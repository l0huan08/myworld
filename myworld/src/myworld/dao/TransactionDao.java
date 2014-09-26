package myworld.dao;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import myworld.entity.Transaction;
import myworld.entity.TransactionType;

/**
 * Operate Transaction table
 * @author huang li
 * 2014.9.19
 */
public class TransactionDao {
	
	private DBConnector dbConnector = new DBConnector();
	
	/**
	 * Get transaction history of an account from fromDate(include) to endDate(include)
	 * @param accountNumber
	 * @param fromDate
	 * @param endDate
	 * @return
	 */
	public List<Transaction> getTransactionRecords(
			String accountNumber,
			Date fromDate, 
			Date endDate) {
		
		// select * from tbTransaction,tbAccount
		//    where tbTransaction.aid=tbAccount.aid
		//      and tbAccount.acnumber='accountNumber'
		//      and tbTransaction.trtime BETWEEN '2012-02-01 00:00:00' AND '2012-11-01 00:00:00'
		
		if (!DaoUtility.isAccountNumberValid(accountNumber))
			return null;

		Connection conn = null;

		try{
			conn = dbConnector.getConnection();
			if (conn==null) //cannot connect to DB
				return null;
			
			PreparedStatement st;
			ResultSet rs;
			
			st = conn.prepareStatement(
					"select tbTransaction.*,trtname from tbTransaction,tbAccount,tbTransactionType " +
					"   where tbTransaction.aid=tbAccount.aid " +
					"      and tbTransaction.trtype=tbTransactionType.trtid" +
		            "      and tbAccount.acnumber=? " +
		            "      and tbTransaction.trtime BETWEEN ? AND ? ");
			st.setString(1, accountNumber);
			st.setDate(2,fromDate);
			st.setDate(3,endDate);
			rs = st.executeQuery();
			
			List<Transaction> lst = new ArrayList<Transaction>();
			// if exist, return true
			while (rs.next()){
				int trid = rs.getInt("trid");
				int aid = rs.getInt("aid");
				
				Timestamp trtimeStamp = rs.getTimestamp("trtime");
				Date trtime = new Date(trtimeStamp.getTime());
				int trtypeId = rs.getInt("trtype");
				String trtypeName=rs.getString("trtname");
				TransactionType trType = new TransactionType(trtypeId,trtypeName);
				double amount = rs.getDouble("amount");
				String description = rs.getString("description");
				
				Transaction transaction = new Transaction(trid,aid,trtime,trType,amount,description);
				lst.add(transaction);
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
}
