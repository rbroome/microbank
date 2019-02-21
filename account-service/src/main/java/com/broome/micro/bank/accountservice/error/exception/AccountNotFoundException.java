package com.broome.micro.bank.accountservice.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends Exception{

	public AccountNotFoundException() {}
	public AccountNotFoundException(String message) {
		super(message);
	}
}
