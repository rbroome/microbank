package com.broome.micro.bank.transactionservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionStatus;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;
import com.broome.micro.bank.transactionservice.repo.TransactionRepository;
import com.broome.micro.bank.transactionservice.restclient.AccountClient;

@Service
public class TransactionService {

	private TransactionRepository transactionRepository;
	
	@Autowired
	private AccountClient accountClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionService.class);

	@Autowired
	public TransactionService(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

	public List<Transaction> getTransactions(String accountNumber) {
		List<Transaction> transactions = transactionRepository.findByFromAccount(accountNumber)
				.orElse(new ArrayList<Transaction>());
		List<Transaction> transactionTo = transactionRepository.findByToAccount(accountNumber)
				.orElse(new ArrayList<Transaction>());
		transactions.addAll(transactionTo);
		
		return transactions;
	}
	
	public Transaction addTransaction(String fromAccount, String toAccount, BigDecimal amount, String message, TransactionType type, Date date) {
		Transaction tr = new Transaction(fromAccount, toAccount, amount, message, type, date);
		//Check if transaction is possible
		checkTransactionAllowed(tr);
		
		
		tr.setStatus(TransactionStatus.PENDING);

		return transactionRepository.save(tr);
		
	}
	
	public void checkTransactionAllowed(Transaction transaction) {
		//TODO: Add a good way to veryidy if it is ok to transger money.
		String hmm = accountClient.authorizeAmount(transaction.getFromAccount(),convertToDTO(transaction));
		LOGGER.info("TRANSACTION OK?? " +hmm);
		//For test, not needed
		if(transaction.getFromAccount().equalsIgnoreCase("123")) {
			return;
		}
		//TODO: Add a good way to veryidy if it is ok to transger money.
		if(true) {
			return;
		}else {
			throw new RuntimeException("Forbidden");
		}
	}
	
	private TransactionDTO convertToDTO(Transaction transaction) {
		TransactionDTO dto = new TransactionDTO(transaction.getFromAccount(), 
				transaction.getToAccount(), transaction.getAmount(), transaction.getMessage(), transaction.getType().name()
				, transaction.getDate());
		
		return dto;
	}
	
	//Used to simulate that transactions is commited, should be done by a "job"
	public void commitTransaction(String accountNumber) {
		getPendingTransactions(accountNumber).forEach(t -> commit(t));;
		
	}
	
	private void commit(Transaction t) {
		t.setStatus(TransactionStatus.COMMITED);
		transactionRepository.save(t);
	}
	
	
	
	public List<Transaction> getPendingTransactions(String accountNumber){
		return getTransactionsWithStatus(accountNumber, TransactionStatus.PENDING);
	}
	public List<Transaction> getCommitedTransactions(String accountNumber){
		return getTransactionsWithStatus(accountNumber, TransactionStatus.COMMITED);
	}
	
	private List<Transaction> getTransactionsWithStatus(String accountNumber, TransactionStatus status) {
		List<Transaction> transactions = transactionRepository.findByFromAccountAndStatus(accountNumber,status)
				.orElse(new ArrayList<Transaction>());
		List<Transaction> transactionTo = transactionRepository.findByToAccountAndStatus(accountNumber,status)
				.orElse(new ArrayList<Transaction>());
		transactions.addAll(transactionTo);
		
		return transactions;
	}
	
	

}
