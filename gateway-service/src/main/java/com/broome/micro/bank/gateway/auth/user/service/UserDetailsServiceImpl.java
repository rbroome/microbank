package com.broome.micro.bank.gateway.auth.user.service;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;
import com.broome.micro.bank.gateway.auth.user.repo.ApplicationUserRepository;



@Service
public class UserDetailsServiceImpl implements BankUserDetailsService{
	private ApplicationUserRepository userRepo;
	
	@Autowired
	public UserDetailsServiceImpl(ApplicationUserRepository userRepo) {
		this.userRepo=userRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser user = userRepo.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException(username);
		
		User userS = new User(user.getUsername(),user.getPassword(),emptyList());
		return userS;
	}
	
	@Override
	public Long getUserIdByName(String username) throws UsernameNotFoundException{
		ApplicationUser user = userRepo.findByUsername(username);
		if(user==null)
			throw new UsernameNotFoundException(username);
		return user.getUserId();
	}
	
}
