package com.broome.micro.bank.controller;




import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.BlockCardDTO;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.PinCodeChangeDTO;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.services.CardService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/")
public class CardController {
	
	private static final Logger log = LoggerFactory.getLogger(CardController.class);
	
	
	//TEMPORRARY!!
	public static final String SECRET = "SecretKEy";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final long EXPIRATION_TIME=864_000_000; // 10 days
	
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/users/sign-up";
	
	@Autowired
	Environment env;
	
	@Autowired
	CardService cardService;
	
	@RequestMapping("/cards")
	public List<Card> getCards(@RequestHeader(value="Authorization") String auth) {
		log.info("GETTING ALL CARDS");
		
		String user = getUserIdFromHeader(auth);
		
		return cardService.findCards(user);
	}
	
	@RequestMapping("/cards/{userId}")
	public List<Card> getCardsForUser(@RequestHeader(value="Authorization") String auth,@PathVariable("userId") String userId) {
		log.info("GETTING ALL CARDS");
		String user = getUserIdFromHeader(auth);
		return cardService.findCards(user);
	}
	
	@RequestMapping(value= "/cards/payment",method = RequestMethod.POST)
	public TransactionDTO processPayment(@RequestBody CardPaymentDTO cardPayment) throws Exception{
		//Verify pin and cardnumber then process the transaciont
		
		return cardService.processPayment(cardPayment);
	}
	
	//TODO: remove to only accountnumber.
	@RequestMapping(path="/cards",method = RequestMethod.POST)
	public Card createNewCard(@RequestHeader(value="Authorization") String auth,@RequestBody Card card) throws Exception {
		log.info("POST: Creating a new card");
		String user = getUserIdFromHeader(auth);
		return cardService.createNew(card.getAccountNumber(), user);
	}
	
	@RequestMapping(path="/cards",method = RequestMethod.PATCH)
	public Card updatePinCode(@RequestBody PinCodeChangeDTO changePin) {
		
		return cardService.updatePincode(changePin.getCardNumber(), changePin.getPinCode(), changePin.getNewPincode());
	}
	
	//TODO: change dto
	@RequestMapping(path="/cards/block",method = RequestMethod.PUT)
	public void block(@RequestHeader(value="Authorization") String auth,@RequestBody BlockCardDTO blockCard) {
		String user = getUserIdFromHeader(auth);
		log.info("BLOCKING CARD id{},cardNumber{},accnumber{}",blockCard.getUserId(),blockCard.getCardNumber(),blockCard.getAccountNumber());
		cardService.blockCard(user, Long.valueOf(blockCard.getCardNumber()), blockCard.getAccountNumber());
		
	}
	
	private String getUserIdFromHeader(String header) {
		log.info("header in cardcontroller: "+header);
		log.info("stripped header: "+header.replaceAll(TOKEN_PREFIX, ""));
		String userId = (String)Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(header.replaceAll(TOKEN_PREFIX, ""))
				.getBody()
				.get("userId");
		
		return userId;
	}

}
