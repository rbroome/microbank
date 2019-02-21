package com.broome.micro.bank.accountservice.dto.account;

public class AuthorizeAmountResponseDTO {
	
	boolean allowed;
	
	public AuthorizeAmountResponseDTO() {}
	public AuthorizeAmountResponseDTO(boolean allowed) {
		this.allowed = allowed;
	}

	public boolean isAllowed() {
		return allowed;
	}

	public void setAllowed(boolean allowed) {
		this.allowed = allowed;
	}
	
	
	

}
