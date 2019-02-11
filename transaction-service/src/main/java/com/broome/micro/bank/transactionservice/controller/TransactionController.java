package com.broome.micro.bank.transactionservice.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.services.TransactionService;



@RestController
@RequestMapping("/")
public class TransactionController {

	@Autowired
	Environment env;
	
	@Autowired
	TransactionService transactionService;
	
	@RequestMapping("/transactions")
	public List<Transaction> getTransactions() {
		
		transactionService.addTransaction("1234", "3211", new BigDecimal("15.5"), "Savings", TransactionType.TransferTransaction, new Date());
		transactionService.addTransaction("1234", "3211", new BigDecimal("14.5"), "Savings", TransactionType.TransferTransaction, new Date());
		transactionService.addTransaction("1234", "3211", new BigDecimal("25.5"), "Savings", TransactionType.TransferTransaction, new Date());
		transactionService.addTransaction("3211", "1234", new BigDecimal("15.5"), "Savings", TransactionType.TransferTransaction, new Date());
		
		return transactionService.getTransactions("1234");
	}
	
	@RequestMapping(value= "/transactions",method = RequestMethod.POST)
	public String addTransaction(@RequestBody Transaction t) throws Exception{
		Transaction transaction = transactionService.addTransaction(t.getFromAccount(), t.getToAccount(), t.getAmount(), t.getMessage(), TransactionType.CardTransaction, new Date());
		return transaction.toString();
	}
	
	@RequestMapping(path = "/transactions/{accountId}")
	public List<Transaction> getTransactionsForAccount(@PathVariable(value = "accountId") long accountId) {
		//Get UserId From UserAuth Service then get account:
		return transactionService.getTransactions(""+accountId);
		
	}
	
	
	//TEMP
	@RequestMapping(path = "/commit/{accountId}")
	public void commitForAccount(@PathVariable(value = "accountId") long accountId) {
		//Only system should be able to do this
		transactionService.commitTransaction(""+accountId);
		
	}
}
