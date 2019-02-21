package com.broome.micro.bank.accountservice.helper;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.account.AccountDTO;

public class AccountHelper {
	
	public static AccountDTO from(Account account) {
		AccountDTO dto = new AccountDTO();
		dto.setName(account.getName());
		dto.setAmount(account.getAmount());
		dto.setPendingAmount(account.getPendingAmount());
		dto.setAccountNumber(account.getAccountNumber());
		return dto;
	}

}
