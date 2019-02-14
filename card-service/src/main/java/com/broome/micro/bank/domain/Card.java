package com.broome.micro.bank.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
//Define a sequence - might also be in another class:
@SequenceGenerator(name="seq", initialValue=123456, allocationSize=100)
public class Card {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
	private long cardNumber;
	
	@Column
	private String pinCode;
	
	@Column
	private String accountNumber;
	
	@Column
	private String userId;
	
	@Column
	private Boolean blocked;
	
	public Card() {
		// TODO Auto-generated constructor stub
	}
	

	public Card(String pinCode, String accountNumber, String userId, Boolean blocked) {
		super();
		this.pinCode = pinCode;
		this.accountNumber = accountNumber;
		this.userId = userId;
		this.blocked = blocked;
	}



	public long getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getBlocked() {
		return blocked;
	}

	public void setBlocked(Boolean blocked) {
		this.blocked = blocked;
	}
	
	@Override
	public String toString() {
		return "pinCode;"+pinCode+"\n" + 
		"accountNumber;"+accountNumber+"\n" + 
		"userId:"+userId+"\n" + 
		"blocked:"+blocked+"\n"+
		"cardNumber"+cardNumber;
		
	}
	
	

}
