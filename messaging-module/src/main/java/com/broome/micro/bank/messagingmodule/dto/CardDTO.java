package com.broome.micro.bank.messagingmodule.dto;

public class CardDTO {
	
	
	private long cardNumber;
	
	
	private String pinCode;
	
	
	private String accountNumber;
	
	
	private String userId;
	
	
	private Boolean blocked;
	
	public CardDTO() {
		// TODO Auto-generated constructor stub
	}
	

	public CardDTO(String accountNumber, String userId) {
		super();
		this.accountNumber = accountNumber;
		this.userId = userId;
	}



	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	
	

}