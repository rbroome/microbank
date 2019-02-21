package com.broome.micro.bank.end2endtests.dto.cards;

public class CardDTO {
	
	
	private long cardNumber;
	
	
	private String pinCode;
	
	
	private String accountNumber;
	
	private Boolean blocked;
	
	public CardDTO() {
		// TODO Auto-generated constructor stub
	}
	

	public CardDTO(String accountNumber) {
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

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	
	

}