package com.broome.micro.bank.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.CardPaymentDTO;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.repo.CardRepository;
import com.broome.micro.bank.restclient.TransactionClient;

@Service
public class CardService {

	private CardRepository cardRepository;
	@Autowired
	private TransactionClient transactionClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

	@Autowired
	public CardService(CardRepository cardRepository) {
		this.cardRepository = cardRepository;
	}

	public Card createNew(String accountNumber, String userId) throws NoSuchElementException {
		// Look if user is valid and is owner of account. Otherwise throw exeption

		// TODO: If User is valid and account is owned by user create card.
		// "Randomly" create a pincode:
		String pinCode = "1234";
		if (accountNumber != null && accountNumber.length() > 0)
			return cardRepository.save(new Card(pinCode, accountNumber, userId, false));
		return null;
	}

	public void blockCard(String userId, long cardnumber, String accountNumber) {
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

	private Card verifyCardWithAccountNumber(long cardId, String userId, String accountNumber) {
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
		if(card.getAccountNumber()==null) {
			transaction.setStatus("DECLINED");
			return transaction;
		}else if (card.getBlocked() == false) {
			LOGGER.info("card {} is not blocked",cardPayment.getCardNumber());
			TransactionDTO response = transactionClient.addTransaction(transaction);
			return response;
		}else{
			LOGGER.info("Card is blocked");
			transaction.setStatus("BLOCKED");
			return transaction;
		}
	}

	private TransactionDTO createTransaction(CardPaymentDTO cardPayment, String accountNumber) {
		TransactionDTO transaction = new TransactionDTO();
		transaction.setAmount(cardPayment.getAmount());

		transaction.setType("CardTransaction");
		transaction.setFromAccount(accountNumber);
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
