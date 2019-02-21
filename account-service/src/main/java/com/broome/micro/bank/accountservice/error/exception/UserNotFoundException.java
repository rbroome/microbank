package com.broome.micro.bank.accountservice.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserNotFoundException extends Exception{

	
	public UserNotFoundException(String message) {
		super(message);
	}
}
