package com.broome.micro.bank.accountservice.dto;

public class CreateCardDTO {
	
	private String pinCode;
	private String userId;
	
	
	public CreateCardDTO() {}
	
	public CreateCardDTO(String pinCode, String userId) {
		super();
		this.pinCode = pinCode;
		this.userId = userId;
	}
	public String getPinCode() {
		return pinCode;
	}
	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
