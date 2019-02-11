package com.broome.micro.bank.transactionservice.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionStatus;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.repo.TransactionRepository;

@Service
public class TransactionService {

	private TransactionRepository transactionRepository;

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
		if(transaction.getFromAccount().equalsIgnoreCase("123")) {
			return;
		}
		if(auth2(transaction)==200) {
			return;
		}else {
			throw new RuntimeException("Forbidden");
		}
	}
	
	private int authorizeAmount(Transaction transaction) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		
		String url = "http://localhost:8080/accounts/"+transaction.getFromAccount()+"/auth/";
		
		HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		LOGGER.info("BODY:"+response);
		
		return response.getStatusCodeValue();

		
	}
	
	
	private int auth2(Transaction transaction) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject map = new JSONObject();
		try {
			map.put("fromAccount", transaction.getFromAccount());
			map.put("toAccount", transaction.getToAccount());
			map.put("amount", transaction.getAmount());
			map.put("message", transaction.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "http://localhost:8080/accounts/"+transaction.getFromAccount()+"/auth/";
		
		HttpEntity<String> entity = new HttpEntity<String>(map.toString(),headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		LOGGER.info("BODY:"+response);
		return response.getStatusCodeValue();

		
	}

	
	//TEMP
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
