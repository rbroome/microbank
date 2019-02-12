package com.broome.micro.bank.accountservice.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.CardDTO;
import com.broome.micro.bank.accountservice.dto.CreateCardDTO;
import com.broome.micro.bank.accountservice.dto.TransactionDTO;
import com.broome.micro.bank.accountservice.repo.AccountRepository;
import com.broome.micro.bank.accountservice.restclient.CardClient;
import com.broome.micro.bank.accountservice.restclient.TransactionClient;

@Service
public class AccountService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

	AccountRepository accountRepository;
	@Autowired
	TransactionClient transactionClient;
	@Autowired
	CardClient cardClient;

	@Autowired
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public List<Account> getAccountsForUser(Long user) {
		return accountRepository.findByUserId(user).orElse(Collections.emptyList());
	}

	public Account getAccount(Long user, Long accountNumber) {
		Optional<Account> account = accountRepository.findByUserIdAndAccountNumber(user, accountNumber);
		Account acc = account.orElseThrow(() -> new NoSuchElementException());
		calculatePendingAmount(acc);
		return acc;
	}

	private BigDecimal calculatePendingAmount(Account account) {
		List<TransactionDTO> transactions = transactionClient
				.getTransactions(account.getAccountNumber().toString());

		BigDecimal amount = transactions.stream()
				.filter(t -> t.getFromAccount().equalsIgnoreCase("" + account.getAccountNumber()))
				.filter(t -> t.getStatus().equalsIgnoreCase("PENDING")).map(t -> t.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal amountTo = transactions.stream()
				.filter(t -> t.getToAccount().equalsIgnoreCase("" + account.getAccountNumber()))
				.filter(t -> t.getStatus().equalsIgnoreCase("PENDING")).map(t -> t.getAmount())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		BigDecimal delta = amountTo.subtract(amount);
		LOGGER.info("Amount: " + delta);
		account.setPendingAmount(delta);
		accountRepository.save(account);
		return delta;
	}

	public void commitTransactions() {
		List<Account> accounts = accountRepository.findAll();
		accounts.forEach(acc -> updateAccount(acc));

	}

	private void updateAccount(Account acc) {
		acc.setAmount(acc.getAmount().add(calculatePendingAmount(acc)));
		acc.setPendingAmount(BigDecimal.ZERO);
		accountRepository.save(acc);
		transactionClient.commitTransactions(acc.getAccountNumber().toString());

	}

	public List<TransactionDTO> getTransactionsForAccount(Account account) {
		// Verify user and account then:
		
		return transactionClient.getTransactions(account.getAccountNumber().toString());
	}

	public Account addAccount(Long userId, String name) {
		Account a = new Account(userId, name);
		return accountRepository.save(a);
	}

	private Account getAccount(Long accountNumber) {
		return accountRepository.findById(accountNumber).orElseThrow(() -> new NoSuchElementException());
	}

	public boolean isAmountApproved(BigDecimal amount, Long accountNumber) {
		//TODO: remove, just to test for being able to adding amounts to accounts
		if(accountNumber == 123)
			return true;
		Account account = getAccount(accountNumber);
		BigDecimal pending = calculatePendingAmount(account);

		BigDecimal total = account.getAmount().add(pending).subtract(amount);
		return total.compareTo(BigDecimal.ZERO) > -1;

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
}
