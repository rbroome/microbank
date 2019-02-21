package com.broome.micro.bank.transactionservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.dto.CommitTransactionResponseDTO;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;
import com.broome.micro.bank.transactionservice.exception.NotAllowedException;
import com.broome.micro.bank.transactionservice.services.TransactionService;

import feign.FeignException;
import io.jsonwebtoken.Jwts;



@RestController
@RequestMapping("/")
public class TransactionController {
	// TEMPORRARY!!
		public static final String SECRET = "SecretKEy";
		public static final String TOKEN_PREFIX = "Bearer ";
		
		private static final Logger log = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	Environment env;
	
	@Autowired
	TransactionService transactionService;
	
	@PostMapping(value= "/transactions")
	@ResponseStatus(HttpStatus.CREATED)
	public TransactionDTO addTransaction(@RequestHeader(value = "Authorization") String auth,@RequestBody TransactionDTO t) throws FeignException{
		isSystemUser(auth);
		return transactionService.addTransaction(t);
	}
	
	@GetMapping(path = "/transactions/{accountId}")
	public List<Transaction> getTransactionsForAccount(@RequestHeader(value = "Authorization") String auth,@PathVariable(value = "accountId") String accountId) {
		isSystemUser(auth);
		return transactionService.getTransactions(accountId);
		
	}
	
	
	//TEMP
	@GetMapping(path = "/transactions/{accountId}/commit")
	public CommitTransactionResponseDTO commitForAccount(@RequestHeader(value = "Authorization") String auth,@PathVariable(value = "accountId") String accountId) {
		//Only system should be able to do this
		isSystemUser(auth);
		return transactionService.commitTransaction(accountId);
		
	}
	
	private boolean isSystemUser(String header) {

		boolean isSystem = (Boolean) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(header.replaceAll(TOKEN_PREFIX, ""))
				.getBody().get("system");
		if(isSystem) {
			return true;
		}
		throw new NotAllowedException();
	}
}
