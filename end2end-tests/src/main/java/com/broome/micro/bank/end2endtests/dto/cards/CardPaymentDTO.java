package com.broome.micro.bank.end2endtests.dto.cards;

import java.math.BigDecimal;

public class CardPaymentDTO {
	
	
	private BigDecimal amount;
	private String cardNumber;
	private String pinCode;
	
	public CardPaymentDTO() {}
	
	public CardPaymentDTO(BigDecimal amount,String cardNumber, String pincode) {
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

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	
	

}
