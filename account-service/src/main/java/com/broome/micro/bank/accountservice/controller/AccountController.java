package com.broome.micro.bank.accountservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.AccountDTO;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountDTO;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.accountservice.dto.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.CardBlockedDTO;
import com.broome.micro.bank.accountservice.dto.CardDTO;
import com.broome.micro.bank.accountservice.dto.CreateCardDTO;
import com.broome.micro.bank.accountservice.dto.TransactionDTO;
import com.broome.micro.bank.accountservice.dto.TransferBetweenAccountsDTO;
import com.broome.micro.bank.accountservice.service.AccountService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/")
public class AccountController {

	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	// TEMPORRARY!!
	public static final String SECRET = "SecretKEy";
	public static final String TOKEN_PREFIX = "Bearer ";

	@Autowired
	Environment env;

	@Autowired
	AccountService accountService;

	@RequestMapping("/accounts")
	public List<Account> getAccounts(@RequestHeader(value = "Authorization") String auth) {
		String userId = getUserIdFromHeader(auth);
		return accountService.getAccountsForUser(userId);

	}

	@RequestMapping("/commit")
	public String commitTransactions() {

		accountService.commitTransactions();

		return "Transactions commited";
	}

	@RequestMapping(path = "/accounts/{accountId}")
	public ResponseEntity<AccountDTO> getAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable(value = "accountId") long accountId) {
		String userId = getUserIdFromHeader(auth);
		AccountDTO account = accountService.getAccount(userId, accountId);

		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(account);
	}

	@RequestMapping(path = "/accounts/{accountId}/transactions")
	public List<TransactionDTO> getTransactionsForAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable(value = "accountId") long accountId) {
		// Get UserId From UserAuth Service then get account:
		String userId = getUserIdFromHeader(auth);
		AccountDTO account = accountService.getAccount(userId, accountId);

		return accountService.getTransactionsForAccount(account);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	public ResponseEntity<Account> addAccount(@RequestHeader(value = "Authorization") String auth,
			@RequestBody AccountDTO acc) throws Exception {

		// get user from Auth then add account:
		String userId = getUserIdFromHeader(auth);
		Account account = accountService.addAccount(userId, acc.getName());

		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(account);
	}

	@RequestMapping(value = "/accounts/{accountNumber}/auth", method = RequestMethod.POST)
	public AuthorizeAmountResponseDTO authorizeAmount(@PathVariable String accountNumber,
			@RequestBody AuthorizeAmountDTO trans) throws Exception {
		Long accNumber = Long.valueOf(accountNumber);

		return accountService.isAmountApproved(trans.getAmount(), accNumber);

	}

	@RequestMapping(value = "/accounts/{accountNumber}/transfer", method = RequestMethod.POST)
	public ResponseEntity<TransactionDTO> transfereAmount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable Long accountNumber, @RequestBody TransferBetweenAccountsDTO transfer) throws Exception {
		log.info("Trying to transfer between {} and {}", accountNumber, transfer.getToAccount());
		if (accountNumber < 37730000) {
			log.info("External authorization..");
			return accountService.transferAmount(transfer, String.valueOf(accountNumber));
		} else {
			String userId = getUserIdFromHeader(auth);
			AccountDTO account = accountService.getAccount(userId, accountNumber);
			return accountService.transferAmount( transfer, String.valueOf(account.getAccountNumber()));
		}

	}

	@RequestMapping(value = "/accounts/{accountNumber}/cards", method = RequestMethod.POST)
	public ResponseEntity<CardDTO> createCardForAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable long accountNumber, @RequestBody CreateCardDTO card) {
		String userId = getUserIdFromHeader(auth);
		AccountDTO account = accountService.getAccount(userId, accountNumber);
		card.setAccountNumber(account.getAccountNumber());
		ResponseEntity<CardDTO> response = accountService.createCardForAccount(card, auth);
		log.info("Response is null: {}", response==null);
		
		return accountService.createCardForAccount(card, auth);
	}

	@RequestMapping(path = "/accounts/{accountNumber}/cards/{cardNumber}", method = RequestMethod.PATCH)
	public ResponseEntity<CardDTO> blockCard(@RequestHeader("Authorization") String token, @PathVariable long accountNumber,@PathVariable String cardNumber,
			@RequestBody CardBlockedDTO blockCard) {
		String userId = getUserIdFromHeader(token);
		AccountDTO account = accountService.getAccount(userId, accountNumber);
		ResponseEntity<CardDTO> response = accountService.blockCardForAccount(account, cardNumber, token);
		log.info("Response is null: {}", response==null);
		if(response!=null) {
			log.info("body is null {}", response.getBody()==null);
		}
		return response;
	}

	private String getUserIdFromHeader(String header) {

		String userId = (String) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(header.replaceAll(TOKEN_PREFIX, ""))
				.getBody().get("userId");

		return userId;
	}

	@GetMapping("/accounts/testDeleteAll")
	public void deleteAllForTest() {
		accountService.removeEverything();
	}
}
