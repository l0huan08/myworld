package myworld.dao;

import java.sql.*;

/**
 * Connect to default Server DB
 * @author huang li
 * 2014.9.18
 */
public class DBConnector {
	private static final String DRIVER = "com.mysql.jdbc.Driver";
	private static final String URL = "jdbc:mysql://127.0.0.1:3306/bank";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "";
	
	private Connection conn = null;
	
	public Connection getConnection() {
		if (conn!=null)
			return conn;
		
		try{
			Class.forName(DRIVER);
			this.conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
		} catch(Exception e){
			e.printStackTrace();
		}
		return this.conn;
	}
	
	public void closeConnection(){
		try{
			if(this.conn != null) {
				this.conn.close();
				this.conn=null;
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
