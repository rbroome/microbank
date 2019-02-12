package com.broome.micro.bank.accountservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.broome.micro.bank.accountservice.dto.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.CardDTO;

@FeignClient("card-service")
public interface CardClient {
	

	@RequestMapping(method=RequestMethod.POST,value="/cards/")
	CardDTO createCard(@RequestBody CardDTO card);
	
	@RequestMapping(method=RequestMethod.PUT,value="/cards/block/")
	String blockCard(@RequestBody BlockCardDTO blockCard);
}
