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
		// TODO: validate with service
		if (card.getAccountNumber() > 37729999) {
			Card response = cardRepository.save(new Card(card.getPinCode(), card.getAccountNumber(), userId, false));
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(null);
		}

	}

	public ResponseEntity<Card> blockCard(String userId, long cardnumber, long accountNumber) {
		// Look if user is owner of card and and account then block it.
		Card card = verifyCardWithAccountNumber(cardnumber, userId, accountNumber);
		card.setBlocked(true);
		card = cardRepository.save(card);
		if (card.getCardNumber() == 0)
			return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(card);

		LOGGER.info("RETURNING BLOCKED CARD {} and it is blocked:{}", card.getCardNumber(), card.getBlocked());
		return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(card);
	}

	public List<Card> findCards(String userId) {
		List<Card> cards = cardRepository.findByUserId(userId).orElse(new ArrayList<>());
		cards.forEach(card -> LOGGER.info("CARDNUMBER: " + card.getCardNumber() + " PIN: " + card.getPinCode()));
		return cards;
	}

	public ResponseEntity<Card> updatePincode(long cardNumber, String oldPinCode, String newPinCode) {
		Optional<Card> card = verifyCard(cardNumber, oldPinCode);
		
		if (card.isPresent()) {
			card.get().setPinCode(newPinCode);
			cardRepository.save(card.get());

			return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).body(card.get());
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(null);
		}
	}

	private Card verifyCardWithAccountNumber(long cardId, String userId, long accountNumber) {
		LOGGER.info("Looking for card {} account {} user {}", cardId, accountNumber, userId);
		Card card = cardRepository.findByUserIdAndCardNumberAndAccountNumber(userId, cardId, accountNumber)
				.orElseThrow(() -> new NoSuchElementException());
		LOGGER.info("found card {} account {} user {}", card.getCardNumber(), card.getAccountNumber(),
				card.getUserId());
		return card;
	}

	public ResponseEntity<TransactionDTO> processPayment(CardPaymentDTO cardPayment) {
		// TODO: Fix this method..
		// Verify pin etc then add the transaction.
		LOGGER.info("Starting to process transaction with card {} and pin {}", cardPayment.getCardNumber(),
				cardPayment.getPinCode());
		// Verify pincode
		Optional<Card> card = verifyCard(cardPayment.getCardNumber(), cardPayment.getPinCode());
		
		if (cardIsValid(card)) {
			TransactionDTO transaction = createTransaction(cardPayment, card.get().getAccountNumber());
			TransactionDTO response = transactionClient.addTransaction(getToken(), transaction);
			return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(response);
		} else {
			LOGGER.info("Card is blocked");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).contentType(MediaType.APPLICATION_JSON).body(null);
		}
	}
	
	private boolean cardIsValid(Optional<Card> card) {
		boolean valid = card.isPresent() && (!card.get().getBlocked() && card.get().getAccountNumber() !=0);
		return valid;
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

	private Optional<Card> verifyCard(long cardNumber, String pincode) {
		Optional<Card> card = cardRepository.findByCardNumberAndPinCode(cardNumber, pincode);
		return card;
	}

	public void removeEverything() {
		LOGGER.info("CLEANING IN SERVICE");
		cardRepository.deleteAll();
	}

}
