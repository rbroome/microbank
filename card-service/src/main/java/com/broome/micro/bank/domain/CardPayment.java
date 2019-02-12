package com.broome.micro.bank.domain;

import java.math.BigDecimal;

public class CardPayment {
	
	private String fromAccount;
	private String message;
	private BigDecimal amount;
	
	public CardPayment() {}
	
	public CardPayment(String fromAccount, String message, BigDecimal amount) {
		super();
		this.fromAccount = fromAccount;
		this.message = message;
		this.amount = amount;
	}

	public String getFromAccount() {
		return fromAccount;
	}

	public void setFromAccount(String fromAccount) {
		this.fromAccount = fromAccount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	

}
