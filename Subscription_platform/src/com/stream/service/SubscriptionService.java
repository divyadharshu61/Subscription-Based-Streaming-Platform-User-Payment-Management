package com.stream.service;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import com.stream.bean.Payment;
import com.stream.bean.Subscription;
import com.stream.bean.User;
import com.stream.dao.*;
import com.stream.util.DBUtil;
import com.stream.util.PendingPaymentsException;
import com.stream.util.SubscriptionAlreadyActiveException;
import com.stream.util.ValidationException;
public class SubscriptionService {
	SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    PaymentDAO paymentDAO = new PaymentDAO();
    UserDAO userDAO = new UserDAO();
    Connection con = null;
	public User viewUserDetails(String userID) {
		if(userID==null) {
			return null;
		}
		User u= userDAO.findUser(userID);
	    return u;
	}
	public List<User> viewAllUsers(){
		UserDAO u=new UserDAO();
		List<User> user=u.viewAllUsers();
	    return user;
	}
	public boolean addNewUser(User user) {
	    if (user == null) {
	        return false;
	    }
	    if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
	        return false;
	    }
	    if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
	        return false;
	    }
	    String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
	    if (!user.getEmail().matches(emailPattern)) {
	        return false;
	    }
	    UserDAO userDAO = new UserDAO();
	    if (userDAO.findUser(user.getUserID()) != null) {
	        return false;   
	    }
	    return userDAO.insertUser(user);
	}
	public boolean removeUser(String userID) throws PendingPaymentsException {
	    if (userID == null || userID.trim().isEmpty()) {
	        return false;
	    }
	    Subscription activeSubscription = subscriptionDAO.findActiveSubscriptionByUser(userID);
	    List<Payment> pendingPayments =paymentDAO.findPendingPaymentsByUser(userID);
	    if (activeSubscription != null || !pendingPayments.isEmpty()) {
	        throw new PendingPaymentsException();
	    }
	    return userDAO.deleteUser(userID);
	}
	public boolean activateSubscription(String userID, String planCode, Date startDate, Date endDate, String paymentMethod) throws ValidationException, SubscriptionAlreadyActiveException {
	    if (userID == null || userID.trim().isEmpty() ||planCode == null || planCode.trim().isEmpty() || startDate == null || endDate == null || startDate.after(endDate) ||paymentMethod == null || paymentMethod.trim().isEmpty()) {
	        throw new ValidationException();
	    }
	    User user = userDAO.findUser(userID);
	    if (user == null) 
	    	{
	    	throw new ValidationException();
	    	}
	    Subscription active = subscriptionDAO.findActiveSubscriptionByUser(userID);
	    if (active != null&&active.getPlanCode().equalsIgnoreCase(planCode)) {
	        throw new SubscriptionAlreadyActiveException();
	    }
	    Connection con = null;
	    try {
	        con = DBUtil.getDBConnection();
	        con.setAutoCommit(false);
	        int subscriptionID = subscriptionDAO.generateSubscriptionID();
	        Subscription sub = new Subscription();
	        sub.setSubscriptionID(subscriptionID);
	        sub.setUserID(userID);
	        sub.setPlanCode(planCode);
	        sub.setStartDate(startDate);
	        sub.setEndDate(endDate);
	        sub.setStatus("ACTIVE");
	        boolean subInserted = subscriptionDAO.recordSubscription(sub);
	        if (!subInserted) {
	            con.rollback();
	            return false;
	        }
	        long paymentID = paymentDAO.generatePaymentID();
	        Payment payment = new Payment();
	        payment.setPaymentID(paymentID);
	        payment.setSubscriptionID(subscriptionID); 
	        payment.setAmount(199.00); 
	        payment.setPaymentDate(new Date(System.currentTimeMillis()));
	        payment.setPaymentMethod(paymentMethod);
	        payment.setStatus("PENDING");
	        boolean payInserted = paymentDAO.recordPayment(payment);
	        if (!payInserted) {
	            con.rollback();
	            return false;
	        }
	        boolean success = simulatePayment(); 
	        if (!success) {
	            paymentDAO.updatePaymentStatus(paymentID, "FAILED");
	            con.rollback();
	            return false;
	        }
	        paymentDAO.updatePaymentStatus(paymentID, "SUCCESS");
	        con.commit();
	        return true;
	    } catch (Exception e) {
	        try {
	        	if (con != null) 
	        		con.rollback();
	        	} catch (Exception ex) {
	        		
	        	}
	        e.printStackTrace();
	        return false;
	    } finally {
	        try { 
	        	if (con != null) 
	        		con.setAutoCommit(true); 
	        	} catch (Exception e) {	
	        	}
	    }
	}
	public boolean simulatePayment() {
        return true;
    }
	public boolean renewSubscription(int subscriptionID, Date newEndDate, String paymentMethod)throws ValidationException {
	    if (subscriptionID <= 0 || newEndDate == null || paymentMethod == null || paymentMethod.trim().isEmpty()) {
	        throw new ValidationException();
	    }
	    Subscription activeSub = subscriptionDAO.findSubscriptionByID(subscriptionID);
	    if (activeSub == null) 
	    	{
	    	return false;
	    	}
	    if (!newEndDate.after(activeSub.getEndDate())) {
	        throw new ValidationException();
	    }
	    Connection con = null;
	    try {
	        con = DBUtil.getDBConnection();
	        con.setAutoCommit(false);
	        long paymentID = paymentDAO.generatePaymentID();
	        Payment payment = new Payment();
	        payment.setPaymentID(paymentID);
	        payment.setSubscriptionID(subscriptionID); 
	        payment.setAmount(199.00);
	        payment.setPaymentDate(new Date(System.currentTimeMillis()));
	        payment.setPaymentMethod(paymentMethod);
	        payment.setStatus("PENDING");
	        if (!paymentDAO.recordPayment(payment)) {
	            con.rollback();
	            return false;
	        }
	        if (!simulatePayment()) {
	            paymentDAO.updatePaymentStatus(paymentID, "FAILED");
	            con.rollback();
	            return false;
	        }
	        paymentDAO.updatePaymentStatus(paymentID, "SUCCESS");
	        con.commit();
	        return true;
	    } catch (Exception e) {
	        try {
	        	if (con != null) 
	        		con.rollback();
	        	}
	        catch (Exception ex) {
	        	 e.printStackTrace();
	        }
	        e.printStackTrace();
	        return false;

	    } 
	    finally {
	        try {
	        	if (con != null)
	        		{
	        		con.setAutoCommit(true); 
	        		}
	        	} catch (Exception e) {
	        	}
	    }
	}
	public boolean processPayment(long paymentID, double amount, String paymentMethod) throws ValidationException{
	    if (paymentID <= 0 || amount <= 0 || paymentMethod == null || paymentMethod.trim().isEmpty()) {
	        throw new ValidationException();
	    }
	    Connection con = null;
	    try {
	        con = DBUtil.getDBConnection();
	        con.setAutoCommit(false);
	        Subscription subscription = subscriptionDAO.findLatestSubscription();
	        if (subscription == null) {
	            System.out.println("No subscription found for payment.");
	            return false;
	        }
	        int subscriptionID = subscription.getSubscriptionID();
	        Payment payment = new Payment();
	        payment.setPaymentID(paymentID);
	        payment.setSubscriptionID(subscriptionID);   
	        payment.setAmount(amount);
	        payment.setPaymentDate(new Date(System.currentTimeMillis()));
	        payment.setPaymentMethod(paymentMethod);
	        payment.setStatus("PENDING");
	        boolean inserted = paymentDAO.recordPayment(payment);
	        if (!inserted) {
	            con.rollback();
	            return false;
	        }
	        boolean success = simulatePayment();
	        if (!success) {
	            paymentDAO.updatePaymentStatus(paymentID, "FAILED");
	            con.rollback();
	            return false;
	        }
	        paymentDAO.updatePaymentStatus(paymentID, "SUCCESS");
	        con.commit();
	        return true;
	    } catch (Exception e) {
	        try { 
	        	if (con != null) 
	        		con.rollback(); 
	        	} catch (Exception ex) {
	        		
	        	}
	        e.printStackTrace();
	        return false;

	    } finally {
	        try { 
	        	if (con != null) 
	        	con.setAutoCommit(true); 
	        	} catch (Exception e) {	
	        	}
	    }
	}

}

