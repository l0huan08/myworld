package myworld.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Operate Admin Table
 * @author huang li
 * 2014.9.19
 */
public class AdminDao {

	private DBConnector dbConnector = new DBConnector();

	/**
	 * admin login
	 * @param username
	 * @param password
	 * @return if username and password is correct, admin can login. return true.
	 */
	public boolean loginAdmin(String username, String password) {
		if (!DaoUtility.isUsernameValid(username))
			return false;

		Connection conn = null;

		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return false;
			
			PreparedStatement st;
			ResultSet rs;
			
			// check does this client's username with password exist?
			st = conn.prepareStatement(
					"select * from tbAdmin where username=? and pw=?");
			st.setString(1, username);
			st.setString(2, password);
			rs = st.executeQuery();
			
			// if exist, return true
			if (rs.next()){
				return true;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {
			try {
				if (conn!=null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
}
