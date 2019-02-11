package com.broome.micro.bank.transactionservice.domain;

public enum TransactionType {
	
	CardTransaction(1),
	TransferTransaction(2);
	
	private final int transactionCode;

	TransactionType(int transactionCode){
		this.transactionCode = transactionCode;
	}
	
	public int getTransactionCode(){
		return this.transactionCode;
	}
}
