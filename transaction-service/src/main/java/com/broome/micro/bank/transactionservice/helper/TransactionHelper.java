package com.broome.micro.bank.transactionservice.helper;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;

public class TransactionHelper {
	
	
	public static TransactionDTO fromTransaction(Transaction t) {
		TransactionDTO dto = new TransactionDTO();
		dto.setAmount(t.getAmount());
		dto.setDate(t.getDate());
		dto.setFromAccount(t.getFromAccount());
		dto.setToAccount(t.getToAccount());
		dto.setMessage(t.getMessage());
		dto.setStatus(t.getStatus().toString());
		dto.setTransactionid(t.getTransactionid());
		dto.setType(t.getType().toString());
		
		return dto;
		
	}

}
