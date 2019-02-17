package com.broome.micro.bank.accountservice.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@SequenceGenerator(name="seq", initialValue=37730000, allocationSize=100)
public class Account {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	private long accountNumber;
	
	@Column
	private String userId;
	@Column
	private BigDecimal amount;
	@Column
	private BigDecimal pendingAmount;
	@Column
	private String name;
	
	public Account() {}
	
	public Account(String userId, String name) {
		super();
		this.userId = userId;
		this.name = name;
		this.amount = BigDecimal.ZERO;
		this.pendingAmount = BigDecimal.ZERO;
	}



	public Long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNr(Long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getPendingAmount() {
		return pendingAmount;
	}

	public void setPendingAmount(BigDecimal pendingAmount) {
		this.pendingAmount = pendingAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	

}
