package com.broome.micro.bank.transactionservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionStatus;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.dto.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.transactionservice.dto.CommitTransactionResponseDTO;
import com.broome.micro.bank.transactionservice.dto.LoginDto;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;
import com.broome.micro.bank.transactionservice.helper.TransactionHelper;
import com.broome.micro.bank.transactionservice.repo.TransactionRepository;
import com.broome.micro.bank.transactionservice.restclient.AccountClient;
import com.broome.micro.bank.transactionservice.restclient.UserClient;

@Service
public class TransactionService {

	private TransactionRepository transactionRepository;

	@Autowired
	private AccountClient accountClient;
	
	@Autowired
	private UserClient userClient;

	private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public List<Transaction> getTransactions(String accountNumber) {
		log.info("Searching for transaction for account {}", accountNumber);
		List<Transaction> finalList = new ArrayList<Transaction>();
		List<Transaction> transactions = transactionRepository.findByFromAccount(accountNumber)
				.orElse(new ArrayList<Transaction>());
		List<Transaction> transactionTo = transactionRepository.findByToAccount(accountNumber)
				.orElse(new ArrayList<Transaction>());
		finalList.addAll(transactions);
		finalList.addAll(transactionTo);
		
		log.info("YFound {} ({}+{})transaction for account \"{}\"", finalList.size(), transactions.size(),
				transactionTo.size(), accountNumber);

		
		return finalList;
	}

	public ResponseEntity<TransactionDTO> addTransaction(TransactionDTO transaction) {
		
		log.info("trying to add transaction from {} to {} with {}",transaction.getFromAccount(),transaction.getToAccount(),transaction.getAmount());
		ResponseEntity<TransactionDTO> trans = addTransactionWithStatus(transaction.getFromAccount(), transaction.getToAccount(),
				transaction.getAmount(), transaction.getMessage(), TransactionType.valueOf(transaction.getType()),
				TransactionStatus.PENDING);

		return trans;
	}

	public TransactionDTO addTransaction(String fromAccount, String toAccount, BigDecimal amount, String message,
			TransactionType type) {
		TransactionDTO trans = addTransactionWithStatus(fromAccount, toAccount, amount, message, type,
				TransactionStatus.PENDING).getBody();

		return trans;
	}

	public ResponseEntity<TransactionDTO> addTransactionWithStatus(String fromAccount, String toAccount, BigDecimal amount, String message,
			TransactionType type, TransactionStatus status) {
		Transaction tr = new Transaction(fromAccount, toAccount, amount, message, type);
		// Check if transaction is possible
		AuthorizeAmountResponseDTO auth = checkTransactionAllowed(tr);

		tr.setStatus(status);
		
		if(auth.isAllowed()) {
			TransactionDTO transaction = TransactionHelper.fromTransaction(transactionRepository.save(tr));
			log.info("Transaction completed with status: "+transaction.getStatus());
			return ResponseEntity.status(HttpStatus.CREATED)
			.contentType(MediaType.APPLICATION_JSON)
			.body(transaction);
		}else {
			log.info("transaction is not allowd");
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.contentType(MediaType.APPLICATION_JSON)
			.body(null);
		}

	}
	

	public AuthorizeAmountResponseDTO checkTransactionAllowed(Transaction transaction) {
		
		String authToken = getToken();
		log.info("authorize transaction from {} to {} with:{} ",transaction.getFromAccount(),transaction.getToAccount(),transaction.getAmount());
		AuthorizeAmountResponseDTO auth = accountClient.authorizeAmount(authToken,transaction.getFromAccount(),
				convertToDTO(transaction));

		// For test, not needed
		if (transaction.getFromAccount().equalsIgnoreCase("123")) {
			log.info("Authorized: true");
			auth.setAllowed(true);
			return auth;
		}
		log.info("Authorized: "+auth.isAllowed());
		return auth;
		
	}
	
	private String getToken() {
		String auth = userClient.login(new LoginDto("sysadmin", "password")).getHeaders().get("Authorization").get(0);
		return auth;
	}

	private TransactionDTO convertToDTO(Transaction transaction) {
		TransactionDTO dto = new TransactionDTO(transaction.getFromAccount(), transaction.getToAccount(),
				transaction.getAmount(), transaction.getMessage(), transaction.getType().name(), transaction.getDate());

		return dto;
	}

	// Used to simulate that transactions is commited, should be done by a "job"
	public CommitTransactionResponseDTO commitTransaction(String accountNumber) {
		AtomicInteger counter = new AtomicInteger();
		CommitTransactionResponseDTO response = new CommitTransactionResponseDTO();
		getPendingTransactions(accountNumber).forEach(t -> {
			commit(t);
			counter.getAndIncrement();
		});
		response.setSuccess(true);
		response.setNumberOfTransactions(counter.get());

		return response;

	}

	private void commit(Transaction t) {
		t.setStatus(TransactionStatus.COMMITTED);
		transactionRepository.save(t);
	}

	public List<Transaction> getPendingTransactions(String accountNumber) {
		return getTransactionsWithStatus(accountNumber, TransactionStatus.PENDING);
	}

	public List<Transaction> getCommitedTransactions(String accountNumber) {
		return getTransactionsWithStatus(accountNumber, TransactionStatus.COMMITTED);
	}

	private List<Transaction> getTransactionsWithStatus(String accountNumber, TransactionStatus status) {
		List<Transaction> transactions = transactionRepository.findByFromAccountAndStatus(accountNumber, status)
				.orElse(new ArrayList<Transaction>());
		List<Transaction> transactionTo = transactionRepository.findByToAccountAndStatus(accountNumber, status)
				.orElse(new ArrayList<Transaction>());
		transactions.addAll(transactionTo);

		return transactions;
	}

	public void removeEveything() {
		transactionRepository.deleteAll();
	}

}
