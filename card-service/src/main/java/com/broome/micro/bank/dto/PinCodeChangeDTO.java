package com.broome.micro.bank.dto;

public class PinCodeChangeDTO {
	
	private String pinCode;
	private String newPincode;
	private Long cardNumber;
	
	public PinCodeChangeDTO() {	}
	
	public PinCodeChangeDTO(String pinCode, String newPincode, Long cardNumber) {
		super();
		this.pinCode = pinCode;
		this.newPincode = newPincode;
		this.cardNumber = cardNumber;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getNewPincode() {
		return newPincode;
	}

	public void setNewPincode(String newPincode) {
		this.newPincode = newPincode;
	}

	public Long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(Long cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	
	
	

}
