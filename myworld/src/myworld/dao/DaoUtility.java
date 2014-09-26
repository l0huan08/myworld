package myworld.dao;

/**
 * The Utility class. Functions including:
 *   Type convertion
 * @author huang li
 * 2014.9.18
 */
class DaoUtility {
	/**
	 * Convert Java Date Type into  Sql Date Type 
	 * @param date  a Java Date object
	 * @return a Sql Date Object
	 */
	//public static java.sql.Date dateToSqlDate(java.util.Date date) {
	//    return new java.sql.Date(date.getTime());
	//}
	
	/**
	 * Valid username
	 * @param username
	 * @return true if username is valid
	 */
	public static boolean isUsernameValid(String username) {
		if (username==null)
			return false;
		if (username.trim().isEmpty())
			return false;
		return true;
	}
	
	/**
	 * Valid username
	 * @param username
	 * @return true if username is valid
	 */
	public static boolean isAccountNumberValid(String accountNumber) {
		if (accountNumber==null)
			return false;
		if (accountNumber.trim().isEmpty())
			return false;
		return true;
	}
}
