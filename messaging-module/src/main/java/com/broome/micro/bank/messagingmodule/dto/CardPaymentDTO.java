package com.broome.micro.bank.messagingmodule.dto;

import java.math.BigDecimal;

public class CardPaymentDTO {
	
	
	private BigDecimal amount;
	private long cardNumber;
	private String pinCode;
	
	public CardPaymentDTO() {}
	
	public CardPaymentDTO(BigDecimal amount,long cardNumber, String pincode) {
		this.amount=amount;
		this.setCardNumber(cardNumber);
		this.setPinCode(pincode);
	}
	

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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
	
	
	

}
