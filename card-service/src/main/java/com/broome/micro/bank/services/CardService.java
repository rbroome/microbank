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

		// If User is valid and account is owned by user create card.
		// "Randomly" create a pincode:
		String pinCode = "1234";
		return cardRepository.save(new Card(pinCode, accountNumber, userId, false));
	}

	public void blockCard(String userId, long cardnumber, String accountNumber) {
		// Look if user is owner of card and and account then block it.
		Card card = verifyCardWithAccountNumber(cardnumber, userId, accountNumber);
		card.setBlocked(false);
		cardRepository.save(card);
	}

	public List<Card> findCards(String userId) {
		return cardRepository.findByUserId(userId).orElse(new ArrayList<>());
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

	public void processPayment(CardPaymentDTO cardPayment) {
		// Verify pin etc then add the transaction.
		TransactionDTO transaction = createTransaction(cardPayment);

		transactionClient.addTransaction(transaction);
	}

	private TransactionDTO createTransaction(CardPaymentDTO cardPayment) {
		TransactionDTO transaction = new TransactionDTO();
		// Verify pincode
		Card card = verifyCard(cardPayment.getCardNumber(), cardPayment.getPinCode());
		if (card.getBlocked() != true) {
			transaction.setAmount(cardPayment.getAmount());

			transaction.setType("CardTransaction");
			transaction.setFromAccount(card.getAccountNumber());
			transaction.setMessage("SOME STORE");
			// Dummy Account
			transaction.setToAccount("123");
			return transaction;
		}
		//TODO: throw exception
		return null;
	}

	private Card verifyCard(long cardNumber, String pincode) {
		Optional<Card> card = cardRepository.findByCardNumberAndPinCode(cardNumber, pincode);
		return card.orElseThrow(() -> new NoSuchElementException());
	}

}
