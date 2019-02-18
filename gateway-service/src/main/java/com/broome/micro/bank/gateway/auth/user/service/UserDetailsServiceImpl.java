package com.broome.micro.bank.gateway.auth.user.service;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;
import com.broome.micro.bank.gateway.auth.user.repo.ApplicationUserRepository;



@Service
public class UserDetailsServiceImpl implements BankUserDetailsService{
	private ApplicationUserRepository userRepo;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserDetailsServiceImpl(ApplicationUserRepository userRepo,BCryptPasswordEncoder passwordEncoder) {
		this.userRepo=userRepo;
		this.passwordEncoder=passwordEncoder;
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
	
	public void createSystemUser() {
		ApplicationUser system = new ApplicationUser();
		system.setUsername("sysadmin");
		system.setPassword(passwordEncoder.encode("password"));
		system.setSystem(true);
		userRepo.save(system);
	}
	
	@Override
	public boolean isSystemUser(Long id) {
		return userRepo.findById(id).get().isSystem();
	}
	
}
