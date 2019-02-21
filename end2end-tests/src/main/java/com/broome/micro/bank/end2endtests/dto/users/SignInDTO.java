package com.broome.micro.bank.end2endtests.dto.users;

public class SignInDTO {

	private String username;
	private String password;
	
	public SignInDTO() {
	}
	
	public SignInDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
