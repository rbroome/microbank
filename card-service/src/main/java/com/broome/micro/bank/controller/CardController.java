package com.broome.micro.bank.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.BlockCardDTO;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.PinCodeChangeDTO;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.services.CardService;

@RestController
@RequestMapping("/")
public class CardController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CardController.class);
	
	@Autowired
	Environment env;
	
	@Autowired
	CardService cardService;
	
	@RequestMapping("/cards")
	public List<Card> getCards() {
		LOGGER.info("GETTING ALL CARDS");
		return cardService.findCards("900120");
	}
	
	@RequestMapping("/cards/{userId}")
	public List<Card> getCardsForUser(@PathVariable("userId") String userId) {
		LOGGER.info("GETTING ALL CARDS");
		return cardService.findCards(userId);
	}
	
	@RequestMapping(value= "/cards/payment",method = RequestMethod.POST)
	public TransactionDTO processPayment(@RequestBody CardPaymentDTO cardPayment) throws Exception{
		//Verify pin and cardnumber then process the transaciont
		
		return cardService.processPayment(cardPayment);
	}
	
	
	@RequestMapping(path="/cards",method = RequestMethod.POST)
	public Card createNewCard(@RequestBody Card card) throws Exception {
		LOGGER.info("POST: Creating a new card");
		return cardService.createNew(card.getAccountNumber(), card.getUserId());
	}
	
	@RequestMapping(path="/cards",method = RequestMethod.PATCH)
	public Card updatePinCode(@RequestBody PinCodeChangeDTO changePin) {
		
		return cardService.updatePincode(changePin.getCardNumber(), changePin.getPinCode(), changePin.getNewPincode());
	}
	
	@RequestMapping(path="/cards/block",method = RequestMethod.PUT)
	public void block(@RequestBody BlockCardDTO blockCard) {
		LOGGER.info("BLOCKING CARD id{},cardNumber{},accnumber{}",blockCard.getUserId(),blockCard.getCardNumber(),blockCard.getAccountNumber());
		cardService.blockCard(blockCard.getUserId(), Long.valueOf(blockCard.getCardNumber()), blockCard.getAccountNumber());
		
	}

}
