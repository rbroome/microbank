package com.broome.micro.bank.accountservice.dto;

public class BlockCardDTO {
	
	String cardNumber;
	String userId;
	
	public BlockCardDTO() {}
	public BlockCardDTO(String cardNumber,String userId) {
		this.cardNumber = cardNumber;
		this.userId = userId;
		
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
	
	
	
	

}
