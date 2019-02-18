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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.BaseIntegrationTest;
import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.BlockCardDTO;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.restclient.TransactionClient;
import com.broome.micro.bank.restclient.UserClient;
import com.broome.micro.bank.services.CardService;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Ignore
public class CardIntegrationTest extends BaseIntegrationTest{
	
	private static final Logger log = LoggerFactory.getLogger(CardIntegrationTest.class);
	static final long OK_ACCOUNT = 37730001;
	private static String OK_HEADER_AUTH="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6IjEiLCJleHAiOjE1NTExMTcyNDV9.rpnO6mjtxmoOFDzC_yE-D8sgQJZz8BaKEvVSrNwiz3St_Q05Y05z-Ixf0TdtI4Avm9t04oqbWkQxb5r4x19VbQ";
	private static String BAD_HEADER_AUTH="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6IjEiLCJleHAiOjE1N2222yNDV9.rpnO6mjtxmoOFDzC_yE-D8sgQJZz8BaKEvVSrNwiz3St_Q05Y05z-Ixf0TdtI4Avm9t04oqbWkQxb1bQ";

	
	private final String SERVER_URL= "http://localhost";
	private final String CARD_ENDPOINT = "/cards";
	
	
	private RestTemplate restTemplate;
	
	@Autowired
	protected TransactionClient client;

	@Autowired
	private CardService cardService;
	
	@Autowired
	private UserClient userClient;
	
	public CardIntegrationTest() {
		restTemplate = new RestTemplate();
	}
	
	
	public void clean() {
		cardService.removeEverything();
		ResponseEntity<String> resp = ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+OK_HEADER_AUTH)
				.body("");
		when(userClient.login(any())).thenReturn(resp);
	}
	
	public ResponseEntity<Card> addCard(String userId,long accountNumber) {
		Card card = new Card();
		
		card.setAccountNumber(accountNumber);
		HttpEntity<Card> entity = setHeaders(card,userId);
		ResponseEntity<Card> returnedCard = restTemplate.exchange(cardsEndpoint(), HttpMethod.POST,entity,Card.class);
		return returnedCard;
		
	}
	
	public List<Card> getCardsForUser(String userId){
		HttpEntity<String> entity = setHeaders(null,userId);
		List<Card> cardResponse=
		restTemplate.exchange(cardsEndpoint()+"/"+userId,HttpMethod.GET,entity, new ParameterizedTypeReference<List<Card>>() {
        }).getBody();
		log.info("got cards:"+cardResponse.size());
		return cardResponse;
	}
	
	private <T> HttpEntity<T> setHeaders(T type ,String userId){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(userId.equalsIgnoreCase("1")) {
			headers.add("Authorization", "Bearer "+OK_HEADER_AUTH);
		}else {
			headers.add("Authorization", "Bearer "+BAD_HEADER_AUTH);
		}
		return new HttpEntity<>(type,headers);
	}
	
	public Card changePin(String userId,String cardNumber,String oldPin,String newPin) {
		return null;
	}
	public Card blockCard(String userId,long cardNumber) {
		BlockCardDTO blockCard = new BlockCardDTO(String.valueOf(cardNumber), OK_ACCOUNT);
		HttpEntity<BlockCardDTO> entity = setHeaders(blockCard, userId);
		restTemplate.exchange(cardsEndpoint()+"/block", HttpMethod.PUT,entity,Card.class);
		List<Card> cards = getCardsForUser(userId);
		return cards.get(0);
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
