package com.broome.micro.bank.accountservice.dto;

import java.math.BigDecimal;

public class AuthorizeAmountDTO {
	
	public BigDecimal amount;
	
	public AuthorizeAmountDTO() {
		// TODO Auto-generated constructor stub
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	

}
