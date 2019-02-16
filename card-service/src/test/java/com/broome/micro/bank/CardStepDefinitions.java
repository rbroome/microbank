package com.broome.micro.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.broome.micro.bank.domain.Card;
import com.broome.micro.bank.dto.TransactionDTO;
import com.broome.micro.bank.helper.CardIntegrationTest;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Ignore
public class CardStepDefinitions extends CardIntegrationTest{

	private final Logger log = LoggerFactory.getLogger(CardStepDefinitions.class);
	private static final String OK_ACCOUNT = "37730001";
	private static final String NOK_ACCOUNT = "";

	private static final String OK_PIN = "1234";
	private static final String OK_CARD = "123456";
	
	private static final String PENDING="PENDING";
	private static final String BLOCKED = "BLOCKED";
	private static final String DECLINED = "DECLINED";
	
	
	
	public CardStepDefinitions() {
		super();
		
	}
	
	@Before
	public void init() {
		clean();
	}

	@Given("^user (.+) have a card$")
	public void haveACard(final String userId) {
		addCard(userId, OK_ACCOUNT);
	}

	@When("^user (.+) list cards$")
	public void listCards(final String userId) {
	}
	

	@When("^the user (.+) adds a card with ok account$")
	public void userAddsACard(final String userId) {
		addCard(userId, OK_ACCOUNT);
	}

	@When("^the user (.+) adds a card with bad account$")
	public void userAddsACardWithBadAccount(final String userId) {
		addCard(userId, NOK_ACCOUNT);
	}

	@Then("^user (.+) have (\\d+) card$")
	public void userHaveCards(final String userId, final int quantity) {
		List<Card> cards = getCardsForUser(userId);
		if(cards == null )
			log.info("CARDS IS NULL");
		else
			log.info("CARDS IS NOT NULL");
		assertThat(cards.size()).isEqualTo(quantity);
	}

	@When("^user (.+) changes pin to (.+)$")
	public void changePin(final String userId, final String newPin) {
		changePin(userId, OK_CARD, OK_PIN, newPin);
	}

	@Then("^user (.+) pin is changed to (.+)$")
	public void userPinIs(final String userId, final String pin) {
		List<Card> cards = getCardsForUser(userId);
		cards.stream().filter(card -> card.getCardNumber() == Long.valueOf(OK_CARD))
				.map(card -> assertThat(card.getPinCode()).isEqualToIgnoringCase(pin));
	}

	@When("^user (.+) blocks the card$")
	public void blockCard(final String userId) {
		blockCardsForUser(userId);
	}

	@Then("^user (.+) card is blocked$")
	public void theCardIsBlocked(final String userId) {
		List<Card> cards = getCardsForUser(userId);
		cards.forEach(card -> log.info("CARD {} is blocked: {}",card.getAccountNumber(),card.getBlocked()));;
		cards.stream().forEach(card -> assertThat(card.getBlocked()).isEqualTo(true));

	}

	@Given("^the card have balance (\\d+)$")
	public void theCardHaveBalance(final int balance) {
		if(balance >0) {
			log.info("SHOULD RETURN GOOD TRANSACTION balance {}",balance);
			when(client.addTransaction(any())).thenReturn(okTransaction(new BigDecimal(balance)));
		}
		else {
			log.info("SHOULD RETURN BAD TRANSACTION balance {}",balance);
			when(client.addTransaction(any())).thenReturn(nokTransaction(new BigDecimal(balance)));
		}

	}

	@When("user (.+) pays (\\d+) with correct pin (.+)")
	public void pay(final String userId, final int amount, final String pin) {
		List<Card> cards = getCardsForUser(userId);
		Card card = cards.get(0);
		TransactionDTO trans = doPayment(userId, new BigDecimal(amount), card.getPinCode(),card.getCardNumber());
		assertThat(trans).isNotNull();
		assertThat(trans.getStatus()).isEqualTo(PENDING);
	}
	
	@Then("user (.+) pays (\\d+) with pin (.+) and its not approved")
	public void payNotApproved(final String userId, final int amount, final String pin) {
		List<Card> cards = getCardsForUser(userId);
		Card card = cards.get(0);
		log.info("CardPin {} actual pin {} cardNr {}"+card.getPinCode(),pin,card.getCardNumber());
		TransactionDTO trans = doPayment(userId, new BigDecimal(amount), pin,card.getCardNumber());
		assertThat(trans).isNotNull();
		assertThat(isBlockedOrDeclined(trans)).isTrue();
	}
	
	private boolean isBlockedOrDeclined(TransactionDTO transaction) {
		boolean isBlocked = transaction.getStatus().equalsIgnoreCase(BLOCKED);
		log.info("ISBLOCKED: " +isBlocked);
		boolean isCanceled = transaction.getStatus().equalsIgnoreCase(DECLINED);
		log.info("ISCANCELED: "+ isCanceled);
		log.info("RETURNING "+(isBlocked || isCanceled));
		return (isBlocked || isCanceled);
	}
	

	@Given("^user (.+) have a blocked card$")
	public void haveABlockedCard(final String userId) {
		addCard(userId, OK_ACCOUNT);
		blockCardsForUser(userId);
	}
	
	private void blockCardsForUser(String userId) {
		List<Card> cards = getCardsForUser(userId);
		cards.forEach(card -> blockCard(userId,card.getCardNumber()));
	}



	

	@Given("^have an account$")
	public void haveAnAccount() {
	}

	@Then("^the payment is approved$")
	public void approvedPayment() {
	}

	@Then("^the payment is not approved$")
	public void declinedPayment() {
	};



}
