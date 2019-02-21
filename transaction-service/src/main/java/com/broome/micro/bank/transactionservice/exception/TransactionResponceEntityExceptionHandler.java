package com.broome.micro.bank.transactionservice.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import feign.FeignException;

@ControllerAdvice
@RestController
public class TransactionResponceEntityExceptionHandler {
	
	
	@ExceptionHandler(FeignException.class)
	  public final ResponseEntity<ErrorDetails> handleFeignException(FeignException ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    
	    
	    return new ResponseEntity<>(errorDetails, HttpStatus.valueOf(ex.status()));
	  }

}
