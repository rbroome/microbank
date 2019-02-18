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
import org.springframework.stereotype.Service;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.accountservice.dto.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.CardDTO;
import com.broome.micro.bank.accountservice.dto.CreateCardDTO;
import com.broome.micro.bank.accountservice.dto.LoginDto;
import com.broome.micro.bank.accountservice.dto.TransactionDTO;
import com.broome.micro.bank.accountservice.exception.AccountNotFoundException;
import com.broome.micro.bank.accountservice.repo.AccountRepository;
import com.broome.micro.bank.accountservice.restclient.CardClient;
import com.broome.micro.bank.accountservice.restclient.TransactionClient;
import com.broome.micro.bank.accountservice.restclient.UserClient;

@Service
public class AccountService {

	private static final Logger log = LoggerFactory.getLogger(AccountService.class);
	private static Long internalAccountStart = 37730000L;

	AccountRepository accountRepository;
	@Autowired
	private TransactionClient transactionClient;
	@Autowired
	CardClient cardClient;
	@Autowired
	UserClient userClient;

	@Autowired
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> getAccountsForUser(String user) {
		return accountRepository.findByUserId(user).orElse(Collections.emptyList());
	}

	public Account getAccount(String user, Long accountNumber) {
		Optional<Account> account = accountRepository.findByUserIdAndAccountNumber(user, accountNumber);
		Account acc = account.orElseThrow(() -> new AccountNotFoundException());
		calculatePendingAmount(acc);
		return acc;
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

	public List<TransactionDTO> getTransactionsForAccount(Account account) {
		// Verify user and account then:
		// getUserIdFromToken(token)
		boolean isAllowed = getAccountsForUser("").stream()
				.filter(acc -> acc.getAccountNumber() == account.getAccountNumber()).collect(Collectors.toList())
				.size() == 1;

		if (isAllowed) {
			String auth = getToken();
			return transactionClient.getTransactions(auth, account.getAccountNumber().toString());
		}else {
			return Collections.emptyList();
		}
	}

	public Account addAccount(String userId, String name) {
		Account a = new Account(userId, name);
		return accountRepository.save(a);
	}

	private Account getAccount(Long accountNumber) {
		return accountRepository.findById(accountNumber).orElseThrow(() -> new NoSuchElementException());
	}

	public AuthorizeAmountResponseDTO isAmountApproved(BigDecimal amount, Long accountNumber) {
		AuthorizeAmountResponseDTO response = new AuthorizeAmountResponseDTO();
		// TODO: remove, just to test for being able to adding amounts to accounts
		if (isExternalAccount(accountNumber)) {
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

	public CardDTO createCardForAccount(CardDTO card) {
		CreateCardDTO cardC = new CreateCardDTO();
		cardC.setPinCode(card.getPinCode());
		cardC.setUserId(card.getUserId());

		return cardClient.createCard(card);
	}

	public String blockCardForAccount(String accountNumber, String userId, String cardNumber) {
		BlockCardDTO blockCard = new BlockCardDTO();
		blockCard.setAccountNumber(accountNumber);
		blockCard.setCardNumber(cardNumber);
		blockCard.setUserId(userId);
		return cardClient.blockCard(blockCard);
	}

	public void removeEverything() {
		accountRepository.deleteAll();
	}
}
