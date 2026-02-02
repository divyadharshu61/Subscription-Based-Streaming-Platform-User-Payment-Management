package com.stream.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.stream.bean.Payment;
import com.stream.util.DBUtil;

public class PaymentDAO {
	public long generatePaymentID() {
		 try {
		        Connection connection = DBUtil.getDBConnection();
		        PreparedStatement ps = connection.prepareStatement("SELECT payment_seq.NEXTVAL from Dual");
		        ResultSet rs = ps.executeQuery();
		        if(rs.next()) {
		          return rs.getLong(1);
		        }
		        }   
		    catch (SQLException e) {
		        e.printStackTrace();
		    }
		return 0;
	}
	public boolean recordPayment(Payment pay) {
		try {
			Connection connection = DBUtil.getDBConnection();
			PreparedStatement ps = connection.prepareStatement("INSERT INTO PAYMENT_TBL (Payment_ID, Subscription_ID, Amount, Payment_Date, Payment_Method, Status) VALUES (?, ?, ?, ?, ?, ?)");
		        ps.setLong(1, pay.getPaymentID());          
		        ps.setInt(2, pay.getSubscriptionID());
		        ps.setDouble(3, pay.getAmount());
		        ps.setDate(4, new Date(pay.getPaymentDate().getTime()));
		        ps.setString(5, pay.getPaymentMethod());
		        ps.setString(6, pay.getStatus());
			 int rs= ps.executeUpdate();
			 if(rs>0) {
				 return true;
			 }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public boolean updatePaymentStatus(long paymentID,String status) {
		try {
			Connection connection = DBUtil.getDBConnection();
		 PreparedStatement ps= connection.prepareStatement("UPDATE PAYMENT_TBL SET Status=? where Payment_ID=?");
		 ps.setString(1,status);
		 ps.setLong(2, paymentID);	
			 int rowsupdate= ps.executeUpdate();
			 if(rowsupdate>0) {
				 return true;
			 }
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	public List<Payment> findPendingPaymentsByUser(String userID){
	    List<Payment> payments = new ArrayList<>();
	    try {
	        Connection connection = DBUtil.getDBConnection();
	        PreparedStatement ps = connection.prepareStatement("SELECT p.* FROM PAYMENT_TBL p JOIN SUBSCRIPTION_TBL s ON p.Subscription_ID = s.Subscription_ID JOIN USER_TBL u ON s.User_ID = u.User_ID WHERE u.User_ID = ? AND p.Status IN ('PENDING', 'FAILED')");
	        ps.setString(1, userID);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Payment payment = new Payment();
	            payment.setPaymentID(rs.getLong("Payment_ID"));
	            payment.setSubscriptionID(rs.getInt("Subscription_ID"));
	            payment.setAmount(rs.getDouble("Amount"));
	            payment.setPaymentDate(rs.getDate("Payment_Date"));
	            payment.setPaymentMethod(rs.getString("Payment_Method"));
	            payment.setStatus(rs.getString("Status"));
	            payments.add(payment);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return payments;
	}

}