package com.broome.micro.bank.gateway.auth.user.controller;

import org.apache.catalina.realm.UserDatabaseRealm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;
import com.broome.micro.bank.gateway.auth.user.repo.ApplicationUserRepository;



@RestController
@RequestMapping("/users")
public class UserController {

	private ApplicationUserRepository userRepo;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserController(ApplicationUserRepository userRepo,BCryptPasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}
	
    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ApplicationUser exists =userRepo.findByUsername(user.getUsername());
        if(exists!=null)
        		throw new RuntimeException("user already exists");
        userRepo.save(user);
    }
}