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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.account.AccountDTO;
import com.broome.micro.bank.accountservice.dto.account.AuthorizeAmountDTO;
import com.broome.micro.bank.accountservice.dto.account.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.accountservice.dto.account.TransferBetweenAccountsDTO;
import com.broome.micro.bank.accountservice.dto.card.CardBlockedDTO;
import com.broome.micro.bank.accountservice.dto.card.CardDTO;
import com.broome.micro.bank.accountservice.dto.card.CreateCardDTO;
import com.broome.micro.bank.accountservice.dto.transaction.TransactionDTO;
import com.broome.micro.bank.accountservice.error.exception.AccountNotFoundException;
import com.broome.micro.bank.accountservice.error.exception.UserNotFoundException;
import com.broome.micro.bank.accountservice.helper.AccountHelper;
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

	@GetMapping("/accounts")
	public List<AccountDTO> getAccounts(@RequestHeader(value = "Authorization") String auth) throws AccountNotFoundException, UserNotFoundException {
		String userId = getUserIdFromHeader(auth);
		return accountService.getAccountsForUser(userId);
	}

	// Used to "simulate" that transaction are commited.
	@RequestMapping("/commit")
	public String commitTransactions() {
		accountService.commitTransactions();
		return "Transactions commited";
	}

	@GetMapping(path = "/accounts/{accountId}")
	public AccountDTO getAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable(value = "accountId") long accountId) throws AccountNotFoundException, UserNotFoundException {
		String userId = getUserIdFromHeader(auth);
		return accountService.getAccount(userId, accountId);
	}

	@GetMapping(path = "/accounts/{accountId}/transactions")
	public List<TransactionDTO> getTransactionsForAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable(value = "accountId") long accountId) throws AccountNotFoundException, UserNotFoundException {
		// Get UserId From UserAuth Service then get account:
		String userId = getUserIdFromHeader(auth);
		AccountDTO account = accountService.getAccount(userId, accountId);

		return accountService.getTransactionsForAccount(userId,account);
	}

	@RequestMapping(value = "/accounts", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public AccountDTO addAccount(@RequestHeader(value = "Authorization") String auth,
			@RequestBody AccountDTO acc) throws Exception {
		String userId = getUserIdFromHeader(auth);
		return accountService.addAccount(userId, acc.getName());
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
			//Simulate a transfer from external account..
			log.info("External authorization..");
			return accountService.transferAmount(transfer, String.valueOf(accountNumber));
		} else {
			String userId = getUserIdFromHeader(auth);
			AccountDTO account = accountService.getAccount(userId, accountNumber);
			return accountService.transferAmount(transfer, String.valueOf(account.getAccountNumber()));
		}
	}

	@RequestMapping(value = "/accounts/{accountNumber}/cards", method = RequestMethod.POST)
	public ResponseEntity<CardDTO> createCardForAccount(@RequestHeader(value = "Authorization") String auth,
			@PathVariable long accountNumber, @RequestBody CreateCardDTO card) throws AccountNotFoundException, UserNotFoundException {
		String userId = getUserIdFromHeader(auth);
		AccountDTO account = accountService.getAccount(userId, accountNumber);
		card.setAccountNumber(account.getAccountNumber());
		ResponseEntity<CardDTO> response = accountService.createCardForAccount(card, auth);
		log.info("Response is null: {}", response == null);

		return accountService.createCardForAccount(card, auth);
	}

	@RequestMapping(path = "/accounts/{accountNumber}/cards/{cardNumber}", method = RequestMethod.PATCH)
	public ResponseEntity<CardDTO> blockCard(@RequestHeader("Authorization") String token,
			@PathVariable long accountNumber, @PathVariable String cardNumber, @RequestBody CardBlockedDTO blockCard) throws AccountNotFoundException, UserNotFoundException {
		String userId = getUserIdFromHeader(token);
		AccountDTO account = accountService.getAccount(userId, accountNumber);
		return accountService.blockCardForAccount(account, cardNumber, token);
	}

	private String getUserIdFromHeader(String header) throws UserNotFoundException {
		String userId = null;
		try {
			userId = (String) Jwts.parser().setSigningKey(SECRET).parseClaimsJws(header.replaceAll(TOKEN_PREFIX, ""))
					.getBody().get("userId");
		} catch (Exception e) {
			throw new UserNotFoundException("User not allowed");
		}
		if(userId.equals("0") || userId.isEmpty()) {
			throw new UserNotFoundException("User not allowed");
		}

		return userId;
	}

	@GetMapping("/accounts/testDeleteAll")
	public void deleteAllForTest() {
		accountService.removeEverything();
	}
}
