package com.broome.micro.bank.helper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.BaseIntegrationTest;
import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.BlockCardDTO;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.restclient.TransactionClient;
import com.broome.micro.bank.services.CardService;

@Ignore
public class RestCommunication extends BaseIntegrationTest{
	
	private static final Logger log = LoggerFactory.getLogger(RestCommunication.class);
	static final String OK_ACCOUNT = "37730001";
	
	private final String SERVER_URL= "http://localhost";
	private final String CARD_ENDPOINT = "/cards";
	
	
	private RestTemplate restTemplate;
	
	@Autowired
	protected TransactionClient client;

	@Autowired
	private CardService cardService;
	
	public RestCommunication() {
		restTemplate = new RestTemplate();
	}
	
	
	public void clean() {
		cardService.removeEverything();
	}
	
	public Card addCard(String userId,String accountNumber) {
		Card card = new Card();
		card.setUserId(userId);
		card.setAccountNumber(accountNumber);
		Card returnedCard = 
		restTemplate.postForEntity(cardsEndpoint(), card, Card.class).getBody();
		return returnedCard;
		
	}
	public List<Card> getCardsForUser(String userId){
		List<Card> cardResponse=
		restTemplate.exchange(cardsEndpoint()+"/"+userId,HttpMethod.GET,null, new ParameterizedTypeReference<List<Card>>() {
        }).getBody();
		log.info("got cards:"+cardResponse);
		return cardResponse;
	}
	
	public Card changePin(String userId,String cardNumber,String oldPin,String newPin) {
		return null;
	}
	public Card blockCard(String userId,long cardNumber) {
		BlockCardDTO blockCard = new BlockCardDTO(String.valueOf(cardNumber), userId, OK_ACCOUNT);
		put(cardsEndpoint()+"/block", blockCard);
		List<Card> cards = getCardsForUser(userId);
		return cards.get(0);
	}
	
	private void put(String url, BlockCardDTO card) {
	    HttpEntity<BlockCardDTO> entity = new HttpEntity<BlockCardDTO>(card);
	    restTemplate.exchange(url, HttpMethod.PUT,entity,Card.class);
	    
	}
	
	public TransactionDTO doPayment(String userId,BigDecimal amount,String pin,long cardnumber) {
		
		
		CardPaymentDTO payment = new CardPaymentDTO(amount, cardnumber, pin);
		TransactionDTO trans = restTemplate.postForEntity(cardsEndpoint()+"/payment", payment, TransactionDTO.class).getBody();
		return trans;
		
	}
	
	protected TransactionDTO okTransaction(BigDecimal amount) {
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAmount(amount);
		transaction.setDate(new Date());
		transaction.setMessage("Some store");
		transaction.setFromAccount("1234");
		transaction.setToAccount("4321");
		transaction.setStatus("PENDING");
		return transaction;
	}
	protected TransactionDTO nokTransaction(BigDecimal amount) {
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAmount(amount);
		transaction.setDate(new Date());
		transaction.setMessage("Some store");
		transaction.setFromAccount("1234");
		transaction.setToAccount("4321");
		transaction.setStatus("DECLINED");
		return transaction;
	}
	
	
	
	private String cardsEndpoint() {
		
		log.info("NEWPORT: "+port);
		return SERVER_URL+":"+port+CARD_ENDPOINT;
	}

}
