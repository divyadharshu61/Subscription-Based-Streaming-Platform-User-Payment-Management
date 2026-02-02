package com.stream.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.stream.bean.User;
import com.stream.util.DBUtil;

public class UserDAO {
	public User findUser(String	userID) {
		User u=null;
		try {
	        Connection connection = DBUtil.getDBConnection();
	        PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_TBL WHERE User_ID = ?");
	        ps.setString(1, userID);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) {
	        	u=new User();
	        	 u.setUserID(rs.getString("User_ID"));
	             u.setFullName(rs.getString("Full_Name"));
	             u.setEmail(rs.getString("Email"));
	             u.setPhone(rs.getString("Phone"));
	             u.setAccountStatus(rs.getString("Account_Status"));
	             u.setCreatedDate(rs.getDate("Created_Date"));
	        }
	        	
	    }   
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
		return u;
	}
	public List<User> viewAllUsers() {
		List<User>user=new ArrayList<>();
		try {
	        Connection connection = DBUtil.getDBConnection();
	        PreparedStatement ps = connection.prepareStatement("SELECT * FROM USER_TBL");
	        ResultSet rs = ps.executeQuery();
	        while(rs.next()) {
	        	User u=new User();
	        	 u.setUserID(rs.getString("User_ID"));
	             u.setFullName(rs.getString("Full_Name"));
	             u.setEmail(rs.getString("Email"));
	             u.setPhone(rs.getString("Phone"));
	             u.setAccountStatus(rs.getString("Account_Status"));
	             u.setCreatedDate(rs.getDate("Created_Date"));
	             user.add(u);
	        }
	        	
	    }   
	    catch (SQLException e) {
	        e.printStackTrace();
	    }
		return user;
	}
	public boolean insertUser(User user) {
		 try {
			Connection connection = DBUtil.getDBConnection();
			PreparedStatement ps= connection.prepareStatement("INSERT INTO USER_TBL (User_ID, Full_Name, Email, Phone, Account_Status, Created_Date)VALUES (?,?,?,?,?,?)");
		ps.setString(1,user.getUserID());
		ps.setString(2,user.getFullName());
		ps.setString(3,user.getEmail());
		ps.setString(4,user.getPhone());
		ps.setString(5,user.getAccountStatus());
		Date d=new Date(user.getCreatedDate().getTime());
		ps.setDate(6,d);
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
	public boolean updateAccountStatus(String userID,String status) {
		try {
			Connection connection = DBUtil.getDBConnection();
		 PreparedStatement ps= connection.prepareStatement("UPDATE USER_TBL SET Account_Status=? where user_ID=?");
		ps.setString(1, status);	
		 ps.setString(2,userID);
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
	public boolean deleteUser(String userID) {
		try {
			Connection connection = DBUtil.getDBConnection();
		 PreparedStatement ps= connection.prepareStatement("DELETE FROM USER_TBL where user_ID=?");
		ps.setString(1, userID);	
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
}