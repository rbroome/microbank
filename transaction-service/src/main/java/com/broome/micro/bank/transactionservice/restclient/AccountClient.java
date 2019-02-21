package com.broome.micro.bank.transactionservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.broome.micro.bank.transactionservice.dto.AuthorizeAmountDTO;

@FeignClient("account-service")
public interface AccountClient {
	
	@RequestMapping(method=RequestMethod.POST,value="/accounts/{accountNumber}/auth/")
	String authorizeAmount(@PathVariable("accountNumber") String accountNumber,@RequestBody AuthorizeAmountDTO transaction);

}
