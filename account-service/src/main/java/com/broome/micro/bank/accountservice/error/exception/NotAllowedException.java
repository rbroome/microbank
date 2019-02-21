package com.broome.micro.bank.accountservice.error.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.annotation.RequestScope;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class NotAllowedException extends Exception{

}
