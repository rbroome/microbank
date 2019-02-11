package com.broome.micro.bank.transactionservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionStatus;

public interface TransactionRepository extends JpaRepository<Transaction,Long>{

	Optional<List<Transaction>> findByFromAccount(String fromAccount);
	Optional<List<Transaction>> findByToAccount(String toAccount);
	
	Optional<List<Transaction>> findByFromAccountAndStatus(String fromAccount,TransactionStatus status);
	Optional<List<Transaction>> findByToAccountAndStatus(String toAccount,TransactionStatus status);
	

}
