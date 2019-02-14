package com.broome.micro.bank.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.broome.micro.bank.dto.TransactionDTO;

@FeignClient("transaction-service")
public interface TransactionClient {

	@RequestMapping(method=RequestMethod.POST,value="/transactions/")
	TransactionDTO addTransaction(@RequestBody TransactionDTO transaction);
}
