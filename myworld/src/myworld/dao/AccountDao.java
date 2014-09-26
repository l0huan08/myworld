package myworld.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import myworld.entity.Account;
import myworld.entity.AccountType;

/**
 * Operate Account Table in DB
 * 
 * @author huang li 2014.9.18
 */
public class AccountDao {

	private DBConnector dbConnector = new DBConnector();
	/** defualt balance in new account */
	private final double NEW_ACCOUNT_BALANCE = 0;
	private final int DEPOSIT_TRANSACTION_TYPE_ID = 1;
	private final int TRANSFER_IN_TRANSACTION_TYPE_ID = 3;
	private final int TRANSFER_OUT_TRANSACTION_TYPE_ID = 4;
	
	/**
	 * Get account
	 * @param accountNumber
	 * @return the account corresponding to this accountNumber
	 */
	public Account getAccount(String accountNumber) {
		if (!DaoUtility.isAccountNumberValid(accountNumber))
			return null;
		
		Connection conn = null;

		try {
			PreparedStatement st;
			ResultSet rs;

			conn = dbConnector.getConnection();
			if (conn == null)
				return null;

			st = conn
					.prepareStatement("select tbAccount.*,typename from tbAccount,"
							+ " tbAccountType "
							+ " where acnumber=? and tbAccount.typeid=tbAccountType.typeid");
			st.setString(1, accountNumber);
			rs = st.executeQuery();

			if (rs.next()) {
				Account account = getAccountFromRecordSet(rs);
				return account;
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

		return null;
	}

	
	/**
	 * Get all account of a client
	 * 
	 * @param username
	 *            client username
	 * @return the list of accounts belong to this client
	 */
	public List<Account> getAccountsByClient(String username) {
		if (!DaoUtility.isUsernameValid(username))
			return null;

		Connection conn = null;

		try {
			PreparedStatement st;
			ResultSet rs;

			conn = dbConnector.getConnection();
			if (conn == null)
				return null;

			st = conn
					.prepareStatement("select tbAccount.*, typename from tbAccount, tbClient, tbAccountType "
							+ "    where tbAccount.cid=tbClient.cid "
							+ "      and tbAccount.typeid=tbAccountType.typeid "
							+ "      and tbClient.username= ?");
			st.setString(1, username);
			rs = st.executeQuery();

			ArrayList<Account> lst = new ArrayList<Account>();
			while (rs.next()) {
				Account account = getAccountFromRecordSet(rs);
				lst.add(account);
			}

			if (lst.size() <= 0)
				return null;
			return lst;

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

		return null;
	}

	/**
	 * Open a new account
	 * 
	 * @param username
	 *            new account's username
	 * @param accountTypeId
	 *            type of the account
	 * @return this new account if successful open. else return null.
	 */
	public Account openAccount(String username, int accountTypeId) {
		if (!DaoUtility.isUsernameValid(username))
			return null;

		Connection conn = null;

		try {
			PreparedStatement st;
			ResultSet rs;
			int cid = 0; // clientId

			conn = dbConnector.getConnection();
			if (conn == null)
				return null;

			// find the cid by username
			st = conn.prepareStatement("select cid from tbClient"
					+ " where username=? ");
			st.setString(1, username);
			rs = st.executeQuery();
			if (rs.next()) {
				cid = rs.getInt("cid");
			} else {
				return null; // not found this client, return null object
			}

			// generate accountNumber
			String acNumber = genNewAccountNumber(cid);

			// insert the account into tbAccount
			st = conn
					.prepareStatement("insert into tbAccount(cid,typeid,acnumber,balance)"
							+ " values(?,?,?,?)");
			st.setInt(1, cid);
			st.setInt(2, accountTypeId);
			st.setString(3, acNumber);
			st.setDouble(4, NEW_ACCOUNT_BALANCE); // default balance = 0
			int rowInserted = st.executeUpdate(); //
			if (rowInserted <= 0) {
				return null; // inserted failed
			}

			// get the inserted account object
			st = conn
					.prepareStatement("select tbAccount.*, typename  from tbAccount,tbAccountType"
							+ " where acnumber=? and tbAccountType.typeid=tbAccount.typeid ");
			st.setString(1, acNumber);
			rs = st.executeQuery();
			if (rs.next()) {
				int aid = rs.getInt("aid");
				String acTypeName = rs.getString("typename");
				double balance = rs.getDouble("balance");
				boolean isactive = rs.getBoolean("isactive");
				AccountType acType = new AccountType(accountTypeId, acTypeName);

				Account account = new Account(aid, cid, acType, balance,
						acNumber, isactive);
				return account; // the new account
			} else {
				return null;// not found account
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

		return null;
	}

	/**
	 * Deposit money into a account with amount of money. 1. Modify the balance
	 * of the account in tbAccount 2. Add a new transaction record into
	 * tbTransaction
	 * 
	 * If the account is Frozen(isactive=false), then cannot deposit.
	 * 
	 * @param accountNumber
	 * @param amount
	 * @return if success deposited, return true.
	 */
	public boolean deposit(String accountNumber, double amount) {
		if (!DaoUtility.isAccountNumberValid(accountNumber))
			return false;

		Connection conn = null;

		try {
			Statement st;
			ResultSet rs;
			String sql;

			conn = dbConnector.getConnection();
			if (conn == null)
				return false;

			// atomic operation
			st = conn.createStatement();

			// get the aid and old balance
			int aid = 0;
			double oldbalance = 0;
			sql = String.format(
					"select * from tbAccount where acnumber='%s' ",
					accountNumber);

			rs = st.executeQuery(sql);
			if (rs.next()) {
				aid = rs.getInt("aid");
				oldbalance = rs.getDouble("balance");
				boolean isactive = rs.getBoolean("isactive");

				// If the account is Frozen(isactive=false), then cannot
				// deposit.
				if (!isactive) {
					return false;
				}
			} else {
				return false;
			}

			// ac.balance += amount.
			// update tbAccount set balance='new balance' where
			// acnumber='acnumber'
			double newBalance = oldbalance + amount;
			sql = String.format(
					"update tbAccount set balance=%f where acnumber='%s' ",
					newBalance, accountNumber);
			st.addBatch(sql);

			// insert a transaction record
			// insert into tbTransaction(aid,trtype,amount,description)
			// values( select aid from tbAccount where acnumber='acnumber',
			// DEPOSIT_TRANSACTION_TYPE_ID,
			// amount, 'deposit 123.4 dollars on 2014-09-19')
			//
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = new java.util.Date();
			String currentDate = dateFormat.format(date); // 2014-08-06

			sql = String.format(
					"insert into tbTransaction(aid,trtype,amount,description) "
							+ "      values( %d, " + "			%d, "
							+ "			%f, 'deposit %.2f dollars on %s' ) ", aid,
					DEPOSIT_TRANSACTION_TYPE_ID, amount, amount, currentDate);

			st.addBatch(sql);

			// Execute transaction
			int[] nRes = st.executeBatch();
			if (nRes[1] > 0)
				return true;
			else
				return false;

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

	/**
	 * Transfer some money to another account. Must-1. Sender and Receiver accounts
	 * must be both Active. 2. Sender have enough money to transfer. 3. Will add
	 * a transaction record for sender account, and a transaction record for
	 * receiver account.
	 * 
	 * @param fromAccountNumber
	 *            Sender of the money
	 * @param toAccountNumber
	 *            Receiver of the money
	 * @param amount
	 *            How much money to transfer
	 * @param memo
	 * 			  Description
	 * @return true if success
	 */
	public boolean makeTransfer(String fromAccountNumber,
			String toAccountNumber, double amount, String memo) {
		if (DaoUtility.isAccountNumberValid(fromAccountNumber)
				&& DaoUtility.isAccountNumberValid(toAccountNumber)) {
		} else
			return false;

		// do transaction
		Connection conn = dbConnector.getConnection();
		if (conn==null)
			return false;
		
		PreparedStatement st;
		String sql;
		ResultSet rs;

		try {
			conn.setAutoCommit(false);
			Savepoint savepnt = conn.setSavepoint();

			try {
				int fromAccountId=0;
				int toAccountId=0;
				String fromName="SECRET USER";
				String toName="SECRET USER";
				String fname,mname,lname;
				
				st = conn.prepareStatement("select tbAccount.aid,balance,isactive,"
						+ "fname,mname,lname from tbAccount, tbClient "
						+ " where acnumber= ? and tbAccount.cid=tbClient.cid");
				st.setString(1, fromAccountNumber);
				rs = st.executeQuery();
				if (rs.next()){
					fromAccountId = rs.getInt("aid");
					double balance = rs.getDouble("balance");
					boolean isactive = rs.getBoolean("isactive");
					fname = rs.getString("fname");
					mname = rs.getString("mname");
					lname = rs.getString("lname");
					fromName = fname + " " + (mname==null?"":mname) + " " +lname;
					
					if (balance<amount || !isactive) { //not enough money or frozen
						return false;
					}
				} else {
					return false;
				}
				
				st = conn.prepareStatement("select aid,isactive,"
						+ " fname,mname,lname from tbAccount,tbClient "
						+ " where acnumber= ? and tbAccount.cid=tbClient.cid");
				st.setString(1, toAccountNumber);
				rs = st.executeQuery();
				if (rs.next()){
					toAccountId = rs.getInt("aid");
					boolean isactive = rs.getBoolean("isactive");
					fname = rs.getString("fname");
					mname = rs.getString("mname");
					lname = rs.getString("lname");
					toName = fname + " " + (mname==null?"":mname) + " " +lname;
					
					if (!isactive) { //frozen
						return false;
					}
				} else {
					return false;
				}
					
				
				// substract balance of fromAccount
				st = conn.prepareStatement(
						"update tbAccount set balance = balance - ? "
								+ " where balance >= ? "
								+ " and acnumber = ? "
								+ " and isactive=TRUE ");
				st.setDouble(1, amount);
				st.setDouble(2, amount);
				st.setString(3, fromAccountNumber);
				int nRs = st.executeUpdate();
				if (nRs <= 0)
				{
					conn.rollback(savepnt);
					return false;
				}
				
				// add balance of toAccount
				st = conn.prepareStatement(
						"update tbAccount set balance = balance + ? "
								+ " where acnumber = ? "
								+ " and isactive=TRUE ");
				st.setDouble(1, amount);
				st.setString(2, toAccountNumber);
				nRs = st.executeUpdate();
				if (nRs <= 0)
				{
					conn.rollback(savepnt);
					return false;
				}
				
				// insert 2 transaction record
				// insert into tbTransaction(aid, trtype, amount, description)
				//   values( select aid from tbAccount where acnumber='acnumber',
				//   DEPOSIT_TRANSACTION_TYPE_ID,
				// amount, 'transfer 123.4 dollars to 3333343 on 2014-09-19')
				//
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = new java.util.Date();
				String currentDate = dateFormat.format(date); // 2014-08-06

				sql = String.format(
						"insert into tbTransaction(aid,trtype,amount,description) "
								+ "      values( %d, " + "			%d, "
								+ "			%f, 'Transfer out %.2f dollars to %s(%s) on %s. MEMO: %s' ) ", 
								fromAccountId,
					TRANSFER_OUT_TRANSACTION_TYPE_ID, amount, amount,
					toAccountNumber, toName, currentDate,memo);
				nRs = st.executeUpdate(sql);
				if (nRs<=0)
				{
					conn.rollback(savepnt);
					return false;
				}
				
				sql = String.format(
						"insert into tbTransaction(aid,trtype,amount,description) "
								+ " values( %d, " + " %d, "
								+ "	%f, 'Transfer in %.2f dollars from %s(%s) on %s. MEMO: %s' ) ", 
								toAccountId,
					TRANSFER_IN_TRANSACTION_TYPE_ID, amount, amount,
					fromAccountNumber, fromName, currentDate,memo);
				nRs = st.executeUpdate(sql);
				if (nRs<=0)
				{
					conn.rollback(savepnt);
					return false;
				}
				
				return true;
				
			} catch (Exception ex) {
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

	/**
	 * Frozen an account
	 * 
	 * @param accountNumber
	 * @return true if success
	 */
	public boolean frozenAccount(String accountNumber) {
		return setAccountActive(accountNumber, false);
	}

	/**
	 * Activate an (frozen) account
	 * 
	 * @param accountNumber
	 * @return
	 */
	public boolean activateAccount(String accountNumber) {
		return setAccountActive(accountNumber, true);
	}

	/**
	 * Delete an account with all its related transactions Only account with
	 * balance=0 can be deleted.
	 * 
	 * @param accountNumber
	 * @return true if success delete this account
	 */
	public boolean deleteAccount(String accountNumber) {
		// check whether this account has no money left or due ( balance=0 )
		// only when balance =0 can delete

		if (!DaoUtility.isAccountNumberValid(accountNumber))
			return false;

		Connection conn = null;
		try {
			conn = dbConnector.getConnection();
			if (conn == null) // cannot connect to DB
				return false;

			PreparedStatement pst;
			String sql;
			ResultSet rs;

			// Search is this account has balance !=0 ?
			sql = "select * from tbAccount where " + "   acnumber= ? "
					+ "   and balance <> 0";

			pst = conn.prepareStatement(sql);
			pst.setString(1, accountNumber);

			rs = pst.executeQuery();
			// if exist non-emply account, then REFUSE to delete client
			if (rs.next()) {
				return false;
			}

			// ---------------- Batch Transaction
			int nRs = 0;
			conn.setAutoCommit(false); // begin Transaction
			Savepoint savepnt = conn.setSavepoint();
			try {
				// remove(from table Transaction where username = 'userName');
				sql = "delete from tbTransaction where aid = "
						+ "(select aid from tbAccount where "
						+ "   acnumber = ? )";

				pst = conn.prepareStatement(sql);
				pst.setString(1, accountNumber);
				pst.executeUpdate();

				// remove(from table Account where acnumber= 'acnumber');
				sql = "delete from tbAccount where acnumber = ? ";
				pst = conn.prepareStatement(sql);
				pst.setString(1, accountNumber);
				nRs = pst.executeUpdate(); // nRs: number of account row deleted

				conn.commit(); // commit and end Transaction
			} catch (Exception ex) {
				conn.rollback(savepnt);
			} finally {
				conn.setAutoCommit(true);
			}
			// ---------------- Execute batch

			return (nRs == 1); // 1 account deleted.

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

	/**
	 * Set account activity
	 * 
	 * @param accountNumber
	 * @param isactive
	 */
	protected boolean setAccountActive(String accountNumber, boolean isactive) {
		if (!DaoUtility.isAccountNumberValid(accountNumber))
			return false;

		Connection conn = null;

		try {
			PreparedStatement st;

			conn = dbConnector.getConnection();
			if (conn == null)
				return false;

			// find the account by accountNumber
			st = conn.prepareStatement("update tbAccount set isactive=? "
					+ " where acnumber=? ");
			st.setBoolean(1, isactive);
			st.setString(2, accountNumber);
			int nRs = st.executeUpdate();

			if (nRs > 0)
				return true;

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

	/**
	 * Generate a new Account Number.
	 * 
	 * @param username
	 *            Client's username
	 * @return the new account number = clientId*1000+random(900)
	 */
	private String genNewAccountNumber(int clientId) {
		// keep 4 digits
		int acNumber = clientId * 1000 + (int) (Math.random() * 900);
		return String.valueOf(acNumber);
	}
	

	private Account getAccountFromRecordSet(ResultSet rs) throws SQLException {
		int aid = rs.getInt("aid");
		int cid = rs.getInt("cid");
		int typeid = rs.getInt("typeid");
		String typeName = rs.getString("typename");
		AccountType acType = new AccountType();
		acType.setTypeId(typeid);
		acType.setTypeName(typeName);

		double balance = rs.getDouble("balance");
		String acnumber = rs.getString("acnumber");
		boolean isactive = rs.getBoolean("isactive");

		Account account = new Account(aid, cid, acType, balance,
				acnumber, isactive);
		return account;
	}
}