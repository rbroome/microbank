package com.broome.micro.bank.end2endtests.dto.accounts;

import java.math.BigDecimal;

public class TransferBetweenAccountsDTO {
	
	private String toAccount;
	private String message;
	private BigDecimal amount;
	public TransferBetweenAccountsDTO(String toAccount, String message, BigDecimal amount) {
		super();
		this.toAccount = toAccount;
		this.message = message;
		this.amount = amount;
	}
	public TransferBetweenAccountsDTO() {
		super();
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
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
	public void setAmount(float amount) {
		this.amount = new BigDecimal(amount);
	}
	

}
