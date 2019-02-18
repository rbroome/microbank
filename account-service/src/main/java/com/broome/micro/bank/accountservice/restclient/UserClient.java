package com.broome.micro.bank.accountservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.broome.micro.bank.accountservice.dto.LoginDto;

@FeignClient("gateway")
public interface UserClient {
	
	@PostMapping(value="/login")
	ResponseEntity<String> login(@RequestBody LoginDto login);

}
