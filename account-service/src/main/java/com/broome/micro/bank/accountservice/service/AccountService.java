package com.broome.micro.bank.accountservice.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.account.AccountDTO;
import com.broome.micro.bank.accountservice.dto.account.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.accountservice.dto.account.TransferBetweenAccountsDTO;
import com.broome.micro.bank.accountservice.dto.card.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.card.CardBlockedDTO;
import com.broome.micro.bank.accountservice.dto.card.CardDTO;
import com.broome.micro.bank.accountservice.dto.card.CreateCardDTO;
import com.broome.micro.bank.accountservice.dto.transaction.TransactionDTO;
import com.broome.micro.bank.accountservice.dto.user.LoginDto;
import com.broome.micro.bank.accountservice.error.exception.AccountNotFoundException;
import com.broome.micro.bank.accountservice.helper.AccountHelper;
import com.broome.micro.bank.accountservice.repo.AccountRepository;
import com.broome.micro.bank.accountservice.restclient.CardClient;
import com.broome.micro.bank.accountservice.restclient.TransactionClient;
import com.broome.micro.bank.accountservice.restclient.UserClient;

@Service
public class AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	private static Long internalAccountStart = 37730000L;

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TransactionClient transactionClient;
	@Autowired
	private CardClient cardClient;
	@Autowired
	private UserClient userClient;

	public List<AccountDTO> getAccountsForUser(String user) throws AccountNotFoundException {
		List<Account> accounts = accountRepository.findByUserId(user)
				.orElseThrow(() -> new AccountNotFoundException("No accounts found for user: " + user));
		List<AccountDTO> dtos = accounts.stream().map(account -> AccountHelper.from(account))
				.collect(Collectors.toList());
		return dtos;
	}

	public AccountDTO getAccount(String user, Long accountNumber) throws AccountNotFoundException {
		Optional<Account> account = accountRepository.findByUserIdAndAccountNumber(user, accountNumber);
		Account acc = account.orElseThrow(() -> new AccountNotFoundException("hoho"));
		calculatePendingAmount(acc);

		return AccountHelper.from(acc);
	}

	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	private String getToken() {
		String auth = userClient.login(new LoginDto("sysadmin", "password")).getHeaders().get("Authorization").get(0);
		return auth;
	}

	private BigDecimal calculatePendingAmount(Account account) {

		String auth = getToken();
		// Todo: get pending transaction from specific account
		List<TransactionDTO> transactions = transactionClient.getTransactions(auth,
				account.getAccountNumber().toString());

		BigDecimal amount = transactions.stream()
				.filter(t -> t.getFromAccount().equalsIgnoreCase("" + account.getAccountNumber()))
				.filter(t -> t.getStatus().equalsIgnoreCase("PENDING")).map(t -> t.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal amountTo = transactions.stream()
				.filter(t -> t.getToAccount().equalsIgnoreCase("" + account.getAccountNumber()))
				.filter(t -> t.getStatus().equalsIgnoreCase("PENDING")).map(t -> t.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal delta = amountTo.subtract(amount);
		log.info("Amount: " + delta);
		account.setPendingAmount(delta);
		accountRepository.save(account);
		return delta;
	}

	public void commitTransactions() {
		List<Account> accounts = accountRepository.findAll();
		accounts.forEach(acc -> updateAccount(acc));

	}

	private void updateAccount(Account acc) {
		String auth = getToken();
		acc.setAmount(acc.getAmount().add(calculatePendingAmount(acc)));
		acc.setPendingAmount(BigDecimal.ZERO);
		accountRepository.save(acc);
		transactionClient.commitTransactions(auth, acc.getAccountNumber().toString());

	}

	public List<TransactionDTO> getTransactionsForAccount(String userId, AccountDTO accountDto)
			throws AccountNotFoundException {
		// Verify user and account then:
		// getUserIdFromToken(token)

		AccountDTO account = getAccount(userId, accountDto.getAccountNumber());

		String auth = getToken();
		return transactionClient.getTransactions(auth, account.getAccountNumber().toString());

	}

	public AccountDTO addAccount(String userId, String name) {
		Account a = new Account(userId, name);
		return AccountHelper.from(accountRepository.save(a));
	}

	private Account getAccount(Long accountNumber) {
		return accountRepository.findById(accountNumber).orElseThrow(() -> new NoSuchElementException());
	}

	public AuthorizeAmountResponseDTO isAmountApproved(BigDecimal amount, Long accountNumber) {
		AuthorizeAmountResponseDTO response = new AuthorizeAmountResponseDTO();

		log.info("Authorize accountnumner {} and amount {}", accountNumber, amount);
		if (isExternalAccount(accountNumber)) {
			log.info("external authorization");
			return handleExternalAuthorization();
		}

		Account account = getAccount(accountNumber);
		BigDecimal pending = calculatePendingAmount(account);

		BigDecimal total = account.getAmount().add(pending).subtract(amount);
		response.setAllowed(total.compareTo(BigDecimal.ZERO) > -1);
		return response;

	}

	private AuthorizeAmountResponseDTO handleExternalAuthorization() {
		// Just return true here so we can have any
		// Movements between accounts
		AuthorizeAmountResponseDTO response = new AuthorizeAmountResponseDTO();
		response.setAllowed(true);
		return response;
	}

	private boolean isExternalAccount(Long accountnumber) {
		return accountnumber <= internalAccountStart;
	}

	public ResponseEntity<CardDTO> createCardForAccount(CreateCardDTO card, String token) {
		return cardClient.createCard(token, card);
	}

	public ResponseEntity<CardDTO> blockCardForAccount(AccountDTO account, String cardNumber, String auth) {
		BlockCardDTO blockCard = new BlockCardDTO();
		blockCard.setAccountNumber(account.getAccountNumber());
		blockCard.setCardNumber(cardNumber);
		log.info("blocking card from account-service");
		return cardClient.blockCard(auth, blockCard);
	}

	public ResponseEntity<TransactionDTO> transferAmount(TransferBetweenAccountsDTO transfer, String fromAccount) {
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAmount(transfer.getAmount());
		transaction.setMessage(transfer.getMessage());
		transaction.setToAccount(transfer.getToAccount());
		transaction.setType("TransferTransaction");
		transaction.setFromAccount(fromAccount);
		log.info("sending transfer to transaction service");
		return transactionClient.addTransaction(getToken(), transaction);
	}

	public void removeEverything() {
		accountRepository.deleteAll();
	}
}
