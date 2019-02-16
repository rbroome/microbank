package com.broome.micro.bank.gateway.auth.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface BankUserDetailsService extends UserDetailsService{
	
	public Long getUserIdByName(String username);

}
