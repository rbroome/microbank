package com.broome.micro.bank.accountservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.AccountDTO;
import com.broome.micro.bank.accountservice.dto.CreateCardDTO;
import com.broome.micro.bank.accountservice.service.AccountService;
import com.broome.micro.bank.messagingmodule.dto.CardDTO;
import com.broome.micro.bank.messagingmodule.dto.TransactionDTO;

@RestController
@RequestMapping("/")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	Environment env;

	@Autowired
	AccountService accountService;

	@RequestMapping("/accounts")
	public List<Account> getAccounts() {

		accountService.addAccount(1l, "Savings");
		accountService.addAccount(2l, "Savings");
		accountService.addAccount(3l, "Debit");

		return accountService.getAccountsForUser(1l);

	}
	
	
	@RequestMapping("/commit")
	public String commitTransactions() {
		
		accountService.commitTransactions();
		
		return "Transactions commited";
	}

	@RequestMapping(path = "/accounts/{accountId}")
	public Account getAccounts(@PathVariable(value = "accountId") long accountId) {
		//Get UserId From UserAuth Service then get account:
		Account account = accountService.getAccount(1l, accountId);
		
		return account;
	}
	@RequestMapping(path = "/accounts/{accountId}/transactions")
	public List<TransactionDTO> getTransactionsForAccount(@PathVariable(value = "accountId") long accountId) {
		//Get UserId From UserAuth Service then get account:
		Account account = accountService.getAccount(1l, accountId);
		
		return accountService.getTransactionsForAccount(account);
	}
	
	@RequestMapping(value= "/accounts",method = RequestMethod.POST)
	public Account addTransaction(@RequestBody AccountDTO acc) throws Exception{
		
		//get user from Auth then add account:
		accountService.addAccount(acc.getUserId(), acc.getName());
		return accountService.addAccount(acc.getUserId(), acc.getName());
	}
	
	@RequestMapping(value= "/accounts/{accountId}/auth",method = RequestMethod.POST)
	public ResponseEntity<Object> authorizeAmount(@RequestBody TransactionDTO trans) throws Exception{
		
		//get user from Auth then add account:
		Long accNumber = Long.valueOf(trans.getFromAccount());
		
		boolean isAllowed = accountService.isAmountApproved(trans.getAmount(),accNumber);
		
		if(isAllowed)
			return ResponseEntity.ok().build();
		else
			return ResponseEntity.notFound().build();
		
	}
	@RequestMapping(value = "/accounts/{accountNumber}/createCard", method = RequestMethod.POST)
	public String createCardForAccount(@PathVariable String accountNumber,@RequestBody CreateCardDTO card) {
		CardDTO newCard = new CardDTO();
		newCard.setAccountNumber(accountNumber);
		newCard.setBlocked(false);
		newCard.setPinCode(card.getPinCode());
		newCard.setUserId(card.getUserId());
		return accountService.createCardForAccount(newCard);
	}
}
