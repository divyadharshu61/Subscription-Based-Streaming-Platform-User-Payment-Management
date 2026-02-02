package com.stream.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static Connection getDBConnection() {
		 try {
	            Class.forName("oracle.jdbc.driver.OracleDriver");

	            String Url = "jdbc:oracle:thin:@localhost:1521:XE";
	            String User = "system";    
	            String Pass = "DIVYA2005.dd";     

	            Connection connection = DriverManager.getConnection(Url, User, Pass);
	            return connection;
	        } catch(ClassNotFoundException | SQLException e) {
	        	e.printStackTrace();
		return null;	
	}
	}
}
