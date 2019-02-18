package com.broome.micro.bank.dto;

public class BlockCardDTO {
	
	String cardNumber;
	long accountNumber;
	
	public BlockCardDTO() {}
	public BlockCardDTO(String cardNumber,long accountNumber) {
		this.cardNumber = cardNumber;
		this.accountNumber = accountNumber;
		
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
		
	}
	
	public long getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	
}
	