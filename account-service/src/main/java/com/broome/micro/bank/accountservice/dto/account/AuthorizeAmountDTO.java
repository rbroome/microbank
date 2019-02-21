package com.broome.micro.bank.accountservice.dto.account;

import java.math.BigDecimal;

public class AuthorizeAmountDTO {
	
	public BigDecimal amount;
	
	public AuthorizeAmountDTO() {
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	

}
