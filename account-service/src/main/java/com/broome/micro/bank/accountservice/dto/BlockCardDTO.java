package com.broome.micro.bank.accountservice.dto;

public class BlockCardDTO {
	
	String cardNumber;
	String accountNumber;
	String userId;
	
	public BlockCardDTO() {}
	public BlockCardDTO(String cardNumber,String userId,String accountNumber) {
		this.cardNumber = cardNumber;
		this.userId = userId;
		this.accountNumber = accountNumber;
		
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
		
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	
	
	

}
