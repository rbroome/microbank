package com.broome.micro.bank.transactionservice.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.dto.CommitTransactionResponseDTO;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;
import com.broome.micro.bank.transactionservice.exception.NotAllowedException;
import com.broome.micro.bank.transactionservice.services.TransactionService;

import io.jsonwebtoken.Jwts;



@RestController
@RequestMapping("/")
public class TransactionController {
	// TEMPORRARY!!
		public static final String SECRET = "SecretKEy";
		public static final String TOKEN_PREFIX = "Bearer ";

	@Autowired
	Environment env;
	
	@Autowired
	TransactionService transactionService;
	
	@RequestMapping("/transactions")
	public List<Transaction> getTransactions() {
		
		//TODO: REMOVE???
		return transactionService.getTransactions("1234");
	}
	
	@RequestMapping(value= "/transactions",method = RequestMethod.POST)
	public ResponseEntity<TransactionDTO> addTransaction(@RequestHeader(value = "Authorization") String auth,@RequestBody TransactionDTO t){
		isSystemUser(auth);
		
		return transactionService.addTransaction(t);
	}
	
	@RequestMapping(path = "/transactions/{accountId}")
	public List<Transaction> getTransactionsForAccount(@RequestHeader(value = "Authorization") String auth,@PathVariable(value = "accountId") String accountId) {
		isSystemUser(auth);
		return transactionService.getTransactions(accountId);
		
	}
	
	
	//TEMP
	@RequestMapping(path = "/transactions/{accountId}/commit")
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
