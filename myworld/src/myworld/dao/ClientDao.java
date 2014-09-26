package myworld.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import myworld.entity.*;


/**
 * Operate the Client table
 * @author huang li
 * 2014.9.18
 */
public class ClientDao {
	/**
	 * Default Client Password after reset
	 */
	private final String DEFAULT_RESET_CLIENT_PASSWORD = "1111";
	
	private DBConnector dbConnector = new DBConnector();
	
	/**
	 * Register a new client.
	 * Set its attributes from front page's textboxes
	 * @return  if the client exist, do not register, return false.
	 *  if success, add a new client record into Client table, and return true. 
	 */
	public boolean registerClient(Client client) {
		
		if (!DaoUtility.isUsernameValid(client.getUsername()))
			return false;

		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return false;
			
			PreparedStatement st;
			ResultSet rs;
			
			// check does this client's username exist?
			st = conn.prepareStatement(
					"select * from tbClient where username=? ");
			st.setString(1, client.getUsername());
			rs = st.executeQuery();
			
			// if exist, return false
			if (rs.next()){
				return false; //exist a client in the table has same username
			}
			
			
			// if not, add a new row.
			st = conn.prepareStatement(
					"insert into tbClient(fname,mname,lname,gender,birthday,tel,"
					+ "add1,add2,zip,email,username,pw) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?)");
			st.setString(1, client.getFirstName());
			st.setString(2, client.getMiddleName());
			st.setString(3, client.getLastName());
			st.setString(4, client.getGender());
			
			st.setDate(5, client.getBirthday());
			st.setString(6, client.getTel());
			st.setString(7, client.getAdd1());
			st.setString(8, client.getAdd2());
			st.setString(9, client.getZip());
			st.setString(10, client.getEmail());
			
			st.setString(11, client.getUsername());
			st.setString(12, client.getPassword());
			
			int nInsertedRow = st.executeUpdate(); //the number of rows inserted
			return (nInsertedRow>0); //means a row inserted
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
	
	/**
	 * Client Login
	 * @param username
	 * @param password
	 * @return if username and password is correct, client can login. return true.
	 */
	public boolean loginClient(String username, String password) {
		if (!DaoUtility.isUsernameValid(username))
			return false;

		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return false;
			
			PreparedStatement st;
			ResultSet rs;
			
			// check does this client's username with password exist?
			st = conn.prepareStatement(
					"select * from tbClient where username=? and pw=?");
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
	
	/**
	 * Get a client object by username
	 * @param username
	 * @return the client object if exist
	 */
	public Client getClient(String username) {
		if (!DaoUtility.isUsernameValid(username))
			return null;

		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return null;
			
			PreparedStatement st;
			ResultSet rs;
			
			// check does this client's username exist?
			st = conn.prepareStatement(
					"select * from tbClient where username=? ");
			st.setString(1, username);
			rs = st.executeQuery();
			
			// if exist, return the Client object
			if (rs.next()){

				Client client = getClientFromResultSet(rs);
		
				return client; //exist a client in the table
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
		
		return null;
		
	}
	
	
	/**
	 * Update Client's profile. 
	 * Do not change cid and username
	 * @param username Client username
	 * @param newClientProfile  including client's new profile
	 * @return true if success updated.
	 */
	public boolean updateClientProfile(String username, Client newClient) {
		if (!DaoUtility.isUsernameValid(username))
			return false;

		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return false;
			
			PreparedStatement st;
			
			// update the client with username='username'.
			// do not update cid and username
			st = conn.prepareStatement(
					"update tbClient "
					+ " set fname=?, mname=?, lname=? ,gender=?,birthday=?,tel=?,"
					+ " add1=?,add2=?,zip=?,email=?,pw=? "
					+ " where username=? ");
			st.setString(1, newClient.getFirstName());
			st.setString(2, newClient.getMiddleName());
			st.setString(3, newClient.getLastName());
			st.setString(4, newClient.getGender());
			
			st.setDate(5, newClient.getBirthday());
			st.setString(6, newClient.getTel());
			st.setString(7, newClient.getAdd1());
			st.setString(8, newClient.getAdd2());
			st.setString(9, newClient.getZip());
			st.setString(10, newClient.getEmail());
			
			st.setString(11, newClient.getPassword());
			st.setString(12, username);
			
			int nInsertedRow = st.executeUpdate(); //the number of rows inserted
			return (nInsertedRow>0); //means a row inserted
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
	
	
	/**
	 * Search clients by username or first name or last name
	 * @param usernameOrName  username or first name or last name.
	 *   if usernameOrName=null or empty, then return all clients.
	 * @return the client objects if exist
	 */
	public List<Client> searchClient(String usernameOrName) {
		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return null;
			
			Statement st;
			ResultSet rs;
			String sql;
			
			boolean searchAll = false;
			if (usernameOrName==null )
				searchAll=true;
			
			usernameOrName = usernameOrName.trim();
			if (usernameOrName.isEmpty())
				searchAll=true;
			
			if (searchAll) {
				sql = "select * from tbClient";
			} else {
				sql = "select * from tbClient where "
						+ " username LIKE '%"+usernameOrName+"%' "
						+ " OR fname LIKE '%"+usernameOrName+"%' "
						+ " OR lname LIKE '%"+usernameOrName+"%' ";
			}
			
			// check does this client's username exist?
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			
			List<Client> lst = new ArrayList<Client>();
			// if exist, return the Client objects
			while (rs.next()){
				Client client = getClientFromResultSet(rs);
				lst.add(client);
			}
			if (lst.size()<=0)
				return null;
			else
				return lst;
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
		
		return null;
		
	}

	
	/**
	 * Reset Client's password into Default Password: "1111"
	 * @param username
	 * @return if reset success, return true
	 */
	public boolean resetClientPassword(String username) {
		//update tbClient set pw=DEFAULT_PASSWORD
		
		if (!DaoUtility.isUsernameValid(username))
			return false;
		
		Connection conn=null;
		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return false;
			
			PreparedStatement st;
			String sql;
			
			sql = "update tbClient set pw=? where username=?";
			
			// check does this client's username exist?
			st = conn.prepareStatement(sql);
			st.setString(1, DEFAULT_RESET_CLIENT_PASSWORD);
			st.setString(2, username);
			int nRowUpdated = st.executeUpdate();
			
			return (nRowUpdated>0);
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
	
	/**
	 * Delete client from tbClient
	 * delete Only when this client has no money in any account. 
	 * @param username
	 * @return if successful deleted, return true.
	 */
	public boolean deleteClient(String username) {
		if (!DaoUtility.isUsernameValid(username))
			return false;

		Connection conn = null;
		try {
			conn = dbConnector.getConnection();
			if (conn == null) // cannot connect to DB
				return false;

			Statement st;
			String sql;
			ResultSet rs;
			
			// Search is there any account of client with balance ?
			st = conn.createStatement();
			sql = "select tbAccount.* from tbAccount,tbClient where "
					+ "   tbAccount.cid=tbClient.cid"
					+ "   and tbClient.username='"+ username +"' "
					+ "   and balance <> 0";
			rs = st.executeQuery(sql);
			// if exist non-emply account, then REFUSE to delete client
			if (rs.next()) {
				return false;
			}
			
			// ---------------- Batch Transaction 
			conn.setAutoCommit(false);
			Savepoint savepnt = conn.setSavepoint();
			
			try {
			//remove(from table Transaction where username = 'userName');
			sql = "delete from tbTransaction where aid IN "
					+ "(select aid from tbAccount,tbClient where"
					+ "   tbAccount.cid=tbClient.cid"
					+ "   and tbClient.username='"+ username +"' )";
			st.executeUpdate(sql);
			
			//remove(from table Account where cid = 'cid');
			sql = "delete from tbAccount where cid= "
					+ "(select cid from tbClient where "
					+ "   username='" + username +"' )";
			st.executeUpdate(sql);
			
			//remove(from table Client where username = 'userName');
			sql = "delete from tbClient where "
					+ "   username='" + username +"' ";
			int nRs = st.executeUpdate(sql);
			
			// ---------------- Execute batch
			return (nRs>0); // 1 SQL statements executed.
			
			} catch (Exception ex){
				ex.printStackTrace();
				conn.rollback(savepnt);
			} finally {
				conn.setAutoCommit(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}
	
	
	private Client getClientFromResultSet(ResultSet rs) throws SQLException {
		int cid = rs.getInt("cid");
		String fname = rs.getString("fname");
		String mname = rs.getString("mname");
		String lname = rs.getString("lname");
		String gender = rs.getString("gender");
		Date birthday = rs.getDate("birthday");
		String tel = rs.getString("tel");
		String add1 = rs.getString("add1");
		String add2 = rs.getString("add2");
		String zip = rs.getString("zip");
		String email = rs.getString("email");
		String usname = rs.getString("username");
		String pw = rs.getString("pw");
			
		Client client = new Client(cid, fname, mname,
				lname, gender, birthday, tel,
				 add1,  add2,  zip,  email,
				usname, pw);
		return client;
	}
}
