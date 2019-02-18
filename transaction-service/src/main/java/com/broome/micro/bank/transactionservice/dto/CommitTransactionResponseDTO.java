package com.broome.micro.bank.transactionservice.dto;

public class CommitTransactionResponseDTO {
	
	private boolean success;
	private int numberOfTransactions;

	public CommitTransactionResponseDTO() {}
	public CommitTransactionResponseDTO(int numberOfTransactions,boolean success) {
		super();
		this.success = success;
		this.numberOfTransactions=numberOfTransactions;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getNumberOfTransactions() {
		return numberOfTransactions;
	}

	public void setNumberOfTransactions(int numberOfTransactions) {
		this.numberOfTransactions = numberOfTransactions;
	}

	
}
