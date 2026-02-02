package com.stream.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.stream.bean.Subscription;
import com.stream.util.DBUtil;

public class SubscriptionDAO {
    public int generateSubscriptionID() {
        try {
            Connection connection = DBUtil.getDBConnection();
            PreparedStatement ps =connection.prepareStatement("SELECT subscription_seq.NEXTVAL FROM DUAL");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public boolean recordSubscription(Subscription sub) {
        try {
            Connection connection = DBUtil.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO SUBSCRIPTION_TBL (Subscription_ID, User_ID, Plan_Code, Start_Date, End_Date, Status) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, sub.getSubscriptionID());
            ps.setString(2, sub.getUserID());
            ps.setString(3, sub.getPlanCode());
            ps.setDate(4, new Date(sub.getStartDate().getTime()));
            ps.setDate(5, new Date(sub.getEndDate().getTime()));
            ps.setString(6, sub.getStatus());
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
    public boolean updateSubscriptionStatus(int subscriptionID, String status) {
        try {
            Connection connection = DBUtil.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("UPDATE SUBSCRIPTION_TBL SET Status = ? WHERE Subscription_ID = ?");
            ps.setString(1, status);
            ps.setInt(2, subscriptionID);
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
    public Subscription findActiveSubscriptionByUser(String userID) {
        Subscription s =null;
        try {
            Connection connection = DBUtil.getDBConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM SUBSCRIPTION_TBL WHERE User_ID = ? AND Status = 'ACTIVE'");
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	s =new Subscription();
                s.setSubscriptionID(rs.getInt("Subscription_ID"));
                s.setUserID(rs.getString("User_ID"));
                s.setPlanCode(rs.getString("Plan_Code"));
                s.setStartDate(rs.getDate("Start_Date"));
                s.setEndDate(rs.getDate("End_Date"));
                s.setStatus(rs.getString("Status"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return s;
    }
    public Subscription findLatestSubscription() {
        Subscription s = null;
        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM SUBSCRIPTION_TBL ORDER BY Subscription_ID DESC");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
            	s = new Subscription();
                s.setSubscriptionID(rs.getInt("Subscription_ID"));
                s.setUserID(rs.getString("User_ID"));
                s.setPlanCode(rs.getString("Plan_Code"));
                s.setStartDate(rs.getDate("Start_Date"));
                s.setEndDate(rs.getDate("End_Date"));
                s.setStatus(rs.getString("Status"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public Subscription findSubscriptionByID(int subscriptionID) {
        Subscription s = null;
        try {
            Connection con = DBUtil.getDBConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM SUBSCRIPTION_TBL WHERE Subscription_ID = ?");
            ps.setInt(1, subscriptionID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s = new Subscription();
                s.setSubscriptionID(rs.getInt("Subscription_ID"));
                s.setUserID(rs.getString("User_ID"));
                s.setPlanCode(rs.getString("Plan_Code"));
                s.setStartDate(rs.getDate("Start_Date"));
                s.setEndDate(rs.getDate("End_Date"));
                s.setStatus(rs.getString("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
}