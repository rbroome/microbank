package com.broome.micro.bank.gateway.auth.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.gateway.auth.user.UserDTO;
import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;
import com.broome.micro.bank.gateway.auth.user.repo.ApplicationUserRepository;
import com.broome.micro.bank.gateway.auth.user.service.UserDetailsServiceImpl;



@RestController
@RequestMapping("/users")
public class UserController {

	private ApplicationUserRepository userRepo;
	private BCryptPasswordEncoder passwordEncoder;
	private UserDetailsServiceImpl userService;
	
	@Autowired
	public UserController(ApplicationUserRepository userRepo,BCryptPasswordEncoder passwordEncoder,UserDetailsServiceImpl userService) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
	}
	
    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ApplicationUser exists =userRepo.findByUsername(user.getUsername());
        if(exists!=null)
        		throw new RuntimeException("user already exists");
        
        ApplicationUser realUser = new ApplicationUser();
        realUser.setPassword(user.getPassword());
        realUser.setUsername(user.getUsername());
        realUser.setSystem(false);
        userRepo.save(realUser);
        return ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(null);
    }
    
    @GetMapping("/testDelete")
    public void deleteAll() {
    		userRepo.deleteAll();
    		userService.createSystemUser();
    }
}