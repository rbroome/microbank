package com.broome.micro.bank.accountservice.dto;

public class AccountDTO {
	
	private Long userId;
	
	private String name;

	public AccountDTO() {}
	
	public AccountDTO(Long userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
