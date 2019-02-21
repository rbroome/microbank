package com.broome.micro.bank.accountservice.error.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.broome.micro.bank.accountservice.error.ErrorDetails;
import com.broome.micro.bank.accountservice.error.exception.AccountNotFoundException;
import com.broome.micro.bank.accountservice.error.exception.UserNotFoundException;
import com.broome.micro.bank.accountservice.error.exception.WrongInputException;

@ControllerAdvice
@RestController
public class AccountResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	
	 @ExceptionHandler(AccountNotFoundException.class)
	  public final ResponseEntity<ErrorDetails> handleUserNotFoundException(AccountNotFoundException ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	  }
	 @ExceptionHandler(UserNotFoundException.class)
	  public final ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
	  }
	 @ExceptionHandler(WrongInputException.class)
	  public final ResponseEntity<ErrorDetails> handleUserNotFoundException(WrongInputException ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
	  }
	
	 @ExceptionHandler(Exception.class)
	  public final ResponseEntity<ErrorDetails> handleUserNotFoundException(Exception ex, WebRequest request) {
	    ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
	        request.getDescription(false));
	    return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
	  }
}
