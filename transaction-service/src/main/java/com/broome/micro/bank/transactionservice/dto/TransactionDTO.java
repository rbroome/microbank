package com.broome.micro.bank.transactionservice.dto;


import java.math.BigDecimal;
import java.util.Date;


public class TransactionDTO {


	private long transactionid;

	private String fromAccount;

	private String toAccount;

	private BigDecimal amount;

	private String message;

	private String type;

	private String status;

	private Date date;
	
	public TransactionDTO() {}
	
	public TransactionDTO(String fromAccount, String toAccount, BigDecimal amount, String message, String type,
			Date date) {
		super();
		this.fromAccount = fromAccount;
		this.toAccount = toAccount;
		this.amount = amount;
		this.message = message;
		this.type = type;
		this.date = date;
	}

	public long getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(long transactionid) {
		this.transactionid = transactionid;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getToAccount() {
		return toAccount;
	}

	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
	
}

