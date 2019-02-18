package com.broome.micro.bank.transactionservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.broome.micro.bank.transactionservice.dto.LoginDto;

@FeignClient("gateway")
public interface UserClient {
	
	@PostMapping(value="/login")
	ResponseEntity<String> login(@RequestBody LoginDto login);

}
