package com.broome.micro.bank.gateway.auth.user.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ApplicationUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String username;
    private String password;
    private boolean system;
    public long getUserId() {
        return userId;
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
	public boolean isSystem() {
		return system;
	}
	public void setSystem(boolean system) {
		this.system = system;
	}
}