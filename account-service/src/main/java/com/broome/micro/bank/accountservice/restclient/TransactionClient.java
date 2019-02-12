package com.broome.micro.bank.accountservice.restclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.broome.micro.bank.accountservice.dto.TransactionDTO;

@FeignClient("transaction-service")
public interface TransactionClient {
	
	@RequestMapping(method=RequestMethod.GET,value="/transactions/{accountNumber}")
	List<TransactionDTO> getTransactions(@PathVariable("accountNumber") String accountNumber);

	@RequestMapping(method=RequestMethod.GET,value="/commit/{accountNumber}")
	void commitTransactions(@PathVariable("accountNumber") String accountNumber);
}
