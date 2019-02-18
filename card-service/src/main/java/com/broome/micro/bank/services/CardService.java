package com.broome.micro.bank.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.CreateCardDTO;
import com.broome.micro.bank.dto.LoginDto;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.repo.CardRepository;
import com.broome.micro.bank.restclient.TransactionClient;
import com.broome.micro.bank.restclient.UserClient;

@Service
public class CardService {

	private CardRepository cardRepository;
	@Autowired
	private TransactionClient transactionClient;
	@Autowired
	private UserClient userClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

	@Autowired
	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public ResponseEntity<Card> createNew(CreateCardDTO card, String userId) throws NoSuchElementException {
		//TODO: validate with service
		if(card.getAccountNumber() > 37729999) {
			Card response = cardRepository.save(new Card(card.getPinCode(), card.getAccountNumber(), userId, false));
			return ResponseEntity.status(HttpStatus.CREATED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(response); 
		} 
		else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN)
			.contentType(MediaType.APPLICATION_JSON)
			.body(null);
		}
		
	}

	public void blockCard(String userId, long cardnumber, long accountNumber) {
		// Look if user is owner of card and and account then block it.
		Card card = verifyCardWithAccountNumber(cardnumber, userId, accountNumber);
		card.setBlocked(true);
		cardRepository.save(card);
	}

	public List<Card> findCards(String userId) {
		List<Card> cards = cardRepository.findByUserId(userId).orElse(new ArrayList<>());
		cards.forEach(card -> LOGGER.info("CARDNUMBER: " + card.getCardNumber() + " PIN: " + card.getPinCode()));
		return cards;
	}

	public Card updatePincode(long cardNumber, String oldPinCode, String newPinCode) {
		Card card = null;
		try {
			card = verifyCard(cardNumber, oldPinCode);
			card.setPinCode(newPinCode);
			cardRepository.save(card);

		} catch (NoSuchElementException e) {
			LOGGER.info("Tried to update pin, but didnt wrok");
		}
		return card;
	}

	private Card verifyCardWithAccountNumber(long cardId, String userId, long accountNumber) {
		return cardRepository.findByUserIdAndCardNumberAndAccountNumber(userId, cardId, accountNumber)
				.orElseThrow(() -> new NoSuchElementException());
	}

	public TransactionDTO processPayment(CardPaymentDTO cardPayment) {
		// Verify pin etc then add the transaction.
		LOGGER.info("Starting to process transaction");
		// Verify pincode
		Card card = verifyCard(cardPayment.getCardNumber(), cardPayment.getPinCode());
		LOGGER.info("Found card with pincode {} and cardNr {} ",card.getPinCode(),card.getCardNumber());
		LOGGER.info("pin is oK");
		TransactionDTO transaction = createTransaction(cardPayment, card.getAccountNumber());
		if(card.getAccountNumber()==0) {
			transaction.setStatus("DECLINED");
			return transaction;
		}else if (card.getBlocked() == false) {
			LOGGER.info("card {} is not blocked",cardPayment.getCardNumber());
			String auth = getToken();
			TransactionDTO response = transactionClient.addTransaction(auth,transaction);
			return response;
		}else{
			LOGGER.info("Card is blocked");
			transaction.setStatus("BLOCKED");
			return transaction;
		}
	}
	private String getToken() {
		String auth = userClient.login(new LoginDto("sysadmin", "password")).getHeaders().get("Authorization").get(0);
		return auth;
	}

	private TransactionDTO createTransaction(CardPaymentDTO cardPayment, long accountNumber) {
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAmount(cardPayment.getAmount());

		transaction.setType("CardTransaction");
		transaction.setFromAccount(String.valueOf(accountNumber));
		transaction.setMessage("SOME STORE");
		// Dummy Account
		transaction.setToAccount("123");
		LOGGER.info("RETURNING TRANSACTION");
		return transaction;

	}

	private Card verifyCard(long cardNumber, String pincode) {
		Optional<Card> card = cardRepository.findByCardNumberAndPinCode(cardNumber, pincode);
		return card.orElse(new Card());
	}

	public void removeEverything() {
		LOGGER.info("CLEANING IN SERVICE");
		cardRepository.deleteAll();
	}

}
