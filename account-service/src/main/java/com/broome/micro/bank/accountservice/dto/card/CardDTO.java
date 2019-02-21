package com.broome.micro.bank.accountservice.dto.card;

public class CardDTO {
	
	
	private long cardNumber;
	
	
	private String pinCode;
	
	
	private long accountNumber;
	
	private Boolean blocked;
	
	public CardDTO() {
		// TODO Auto-generated constructor stub
	}
	

	public CardDTO(long accountNumber) {
		super();
		this.accountNumber = accountNumber;
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

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}
	

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	
	

}