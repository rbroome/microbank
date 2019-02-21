package com.broome.micro.bank.accountservice.dto;

import java.math.BigDecimal;

public class AccountDTO {
	
	private String name;
	
	private Long accountNumber;
	
	private BigDecimal amount;
	
	private BigDecimal pendingAmount;

	public AccountDTO() {}

	public AccountDTO(String name, Long accountNumber, BigDecimal amount, BigDecimal pendingAmount) {
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

	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(Long accountNumber) {
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
