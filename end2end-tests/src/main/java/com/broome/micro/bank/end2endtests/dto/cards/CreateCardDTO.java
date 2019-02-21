package com.broome.micro.bank.end2endtests.dto.cards;

public class CreateCardDTO {
	
	private String pinCode;
	private long accountNumber;
	
	
	public CreateCardDTO() {}
	
	public CreateCardDTO(String pinCode, String userId,long accountNumber) {
		super();
		this.pinCode = pinCode;
		this.accountNumber = accountNumber;
	}
	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	
	

}
