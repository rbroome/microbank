package com.broome.micro.bank.end2endtests.dto.accounts;

import java.math.BigDecimal;

public class AccountDTO {
	
	private String name;
	
	private String accountNumber;
	
	private BigDecimal amount;
	
	private BigDecimal pendingAmount;

	public AccountDTO() {}

	public AccountDTO(String name, String accountNumber, BigDecimal amount, BigDecimal pendingAmount) {
		super();
		this.name = name;
		this.accountNumber = accountNumber;
		this.amount = amount;
		this.pendingAmount = pendingAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}
	
		
	

}
