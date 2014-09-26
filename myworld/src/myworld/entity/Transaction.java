package myworld.entity;

import java.sql.Date;

public class Transaction {
	private int transactionId=0; //transaction id
	private int accountId=0;
	private Date transactionTime=null; //transaction time
	private TransactionType transactionType=null; //transaction type id
	private double amount=0;
	private String description=null;
	
	public Transaction(int transactionId, int accountId, Date transactionTime,
			TransactionType transactionType, double amount, String description) {
		this.transactionId = transactionId;
		this.accountId = accountId;
		this.transactionTime = transactionTime;
		this.transactionType = transactionType;
		this.amount = amount;
		this.setDescription(description);
	}
	
	public Transaction() {
		
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	public int getAccountId() {
		return accountId;
	}
	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}
	public Date getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
