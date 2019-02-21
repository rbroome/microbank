package com.broome.micro.bank.accountservice.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.broome.micro.bank.accountservice.dto.card.BlockCardDTO;
import com.broome.micro.bank.accountservice.dto.card.CardDTO;
import com.broome.micro.bank.accountservice.dto.card.CreateCardDTO;

@FeignClient("card-service")
public interface CardClient {
	

	@RequestMapping(method=RequestMethod.POST,value="/cards/")
	ResponseEntity<CardDTO> createCard(@RequestHeader("Authorization") String token,@RequestBody CreateCardDTO card);
	
	@RequestMapping(method=RequestMethod.PUT,value="/cards/block/")
	ResponseEntity<CardDTO> blockCard(@RequestHeader("Authorization") String token,@RequestBody BlockCardDTO blockCard);
}
