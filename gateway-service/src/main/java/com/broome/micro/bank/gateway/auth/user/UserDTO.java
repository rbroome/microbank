package com.broome.micro.bank.gateway.auth.user;

public class UserDTO {
    
	
    private long userId;
    private String username;
    private String password;
    public long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
    	this.userId=userId;
    	
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