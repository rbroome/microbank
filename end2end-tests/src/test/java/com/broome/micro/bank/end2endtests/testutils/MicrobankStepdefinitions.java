package com.broome.micro.bank.end2endtests.testutils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.broome.micro.bank.end2endtests.dto.accounts.AccountDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CardBlockedDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CardDTO;
import com.broome.micro.bank.end2endtests.dto.cards.PinChangedDTO;
import com.broome.micro.bank.end2endtests.dto.transactions.TransactionDTO;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class MicrobankStepdefinitions extends MicrobankApplication {

	private HttpStatus previousResponse;
	private String latest_cardnumber="";
	private String latest_pin="";
	private TransactionDTO latest_transaction=null;

	@Before
	public void init() {
		clean();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@When("user signs up with username (.+) and password (.+)")
	public void user_signs_up_with_username_test_and_password_secret(String username, String password) {
		ResponseEntity<String> response = signUp(username, password);
		assertHttpStatus(response.getStatusCode(), HttpStatus.CREATED);
	}

	@Then("status (\\d+) is returned")
	public void ok_is_returned(Integer expected) {
		assertThat(previousResponse).isEqualTo(HttpStatus.valueOf(expected));
	}

	@When("user logs in with username (.+) and password (.+)")
	public void user_logs_in_with_username_test_and_password_secret(String username, String password) {
		ResponseEntity<String> response = signIn(username, password);
		assertThat(response).isNotNull();
		assertHttpStatus(response.getStatusCode(), HttpStatus.OK);

		assertThat(response.getHeaders().get("Authorization")).isNotNull();
		setAuthToken(response.getHeaders().get("Authorization").get(0));
	}

	@Then("token is returned")
	public void token_is_returned() {
		assertThat(header_auth).isNotNull();
		assertThat(header_auth).isNotBlank();
		assertThat(header_auth).startsWith("Bearer");
	}

	@When("user creates an account")
	public void user_creates_an_account() {
		String name = "blabla";
		ResponseEntity<AccountDTO> resp = createAnAccount(header_auth, name);
		assertThat(resp).isNotNull();
		assertHttpStatus(resp.getStatusCode(), HttpStatus.CREATED);
		assertThat(resp.getBody().getName()).isEqualTo(name);

	}

	@Then("(\\d+) accouns exists")
	public void accouns_exists(Integer quantity) {
		ResponseEntity<List<AccountDTO>> resp = getAccounts(header_auth);
		assertHttpStatus(resp.getStatusCode(), HttpStatus.OK);
		assertThat(resp.getBody().size()).isEqualTo(quantity);
	}

	@Then("user gets his account and can see the balance")
	public void user_gets_his_account_and_can_see_the_balance() {
		AccountDTO account = getFirstAccount(header_auth);
		ResponseEntity<AccountDTO> response = getAccount(header_auth, account.getAccountNumber());
		assertHttpStatus(response.getStatusCode(), HttpStatus.OK);
		assertThat(response.getBody().getAmount()).isNotNull();

	}

	@Then("user gets his transactions and can see them")
	public void user_gets_his_transactions_and_can_see_them() {
		AccountDTO account = getFirstAccount(header_auth);

		ResponseEntity<List<TransactionDTO>> response = getTransactionsForAccount(header_auth,
				account.getAccountNumber());
		assertHttpStatus(response.getStatusCode(), HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();

	}

	private AccountDTO getFirstAccount(String token) {
		ResponseEntity<List<AccountDTO>> resp = getAccounts(header_auth);
		assertHttpStatus(resp.getStatusCode(), HttpStatus.OK);
		assertThat(resp.getBody().size()).isEqualTo(1);
		AccountDTO account = resp.getBody().get(0);
		return account;
	}

	@Then("user can transfer money bewteen his accounts")
	public void user_can_transfer_money_bewteen_his_accounts() {
		List<AccountDTO> accounts = getAccounts(header_auth).getBody();
		assertThat(accounts.size()).isGreaterThanOrEqualTo(2);
		String fromAccount = accounts.get(0).getAccountNumber();
		String toAccount = accounts.get(1).getAccountNumber();
		ResponseEntity<TransactionDTO> response = transferBetweenAccounts(header_auth, fromAccount, toAccount, "HELLO",
				new BigDecimal(50));
		assertHttpStatus(response.getStatusCode(), HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		TransactionDTO transaction = response.getBody();
		assertThat(transaction.getFromAccount()).isEqualTo(fromAccount);
		assertThat(transaction.getToAccount()).isEqualTo(toAccount);
		assertThat(transaction.getAmount()).isEqualTo(new BigDecimal(50));
	}

	@Then("user creates a card to that account and its ok")
	public void user_creates_a_card_to_that_account_and_its_ok() {
		AccountDTO account = getFirstAccount(header_auth);
		ResponseEntity<CardDTO> response = createCard(header_auth, "1234", account.getAccountNumber());

		assertHttpStatus(response.getStatusCode(), HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		CardDTO card = response.getBody();
		
		assertThat(card.getBlocked()).isFalse();
		latest_cardnumber=String.valueOf(card.getCardNumber());
		latest_pin = card.getPinCode();
	}

	@Then("user pays (\\d+) with the correct pincode")
	public void user_pays_with_the_correct_pincode_and_the_transaction_is(Integer amount) {
		addMoneyToAccounts(new BigDecimal(500));
		ResponseEntity<TransactionDTO> response = null;
		try {
			response = doCardPayment(header_auth, amount, latest_cardnumber, latest_pin);
		}catch(HttpClientErrorException e) {
			previousResponse = e.getStatusCode();
			return;
		}
		assertThat(response.getStatusCode()).isNotNull();
		assertThat(response.getBody()).isNotNull();
		latest_transaction = response.getBody();
		previousResponse = response.getStatusCode();
		
	}
	@Then("the transaction is (.+)")
	public void lastTransactionIsStatus(String status) {
		assertThat(latest_transaction).isNotNull();
		assertThat(latest_transaction.getStatus()).isEqualTo(status);
		
	}

	@Then("user pays (\\d+) with the wrong pincode")
	public void user_pays_with_the_wrong_pincode_and_the_transaction_is_DECLINED(Integer amount) {
		ResponseEntity<TransactionDTO> response =null;
		try {
			response = doCardPayment(header_auth, amount, latest_cardnumber, "ABCD");
		}catch(HttpClientErrorException e) {
			assertHttpStatus(e.getStatusCode(), HttpStatus.FORBIDDEN);
		}
		assertThat(response).isNull();
		
	}

	@Then("user change the pin from (.+) to (.+) and its ok")
	public void user_change_the_pin_from_to_and_its_ok(String oldpin,String newPin) {
		ResponseEntity<PinChangedDTO> response = changePinCodeForCard(header_auth, latest_pin, newPin, latest_cardnumber);
		assertHttpStatus(response.getStatusCode(), HttpStatus.NO_CONTENT);
	}

	@Then("user blocks the card and its ok")
	public void user_blocks_the_card_and_its_ok() {
		AccountDTO account = getFirstAccount(header_auth);
		ResponseEntity<CardDTO> response = blockCard(header_auth, account.getAccountNumber(),latest_cardnumber);
//		assertHttpStatus(response.getStatusCode(), HttpStatus.NO_CONTENT);
		assertThat(response.getBody()).isNotNull();
		CardDTO card = response.getBody();
		assertThat(card.getBlocked()).isTrue();
	}
	
	@Given("user have (\\d+) on all his accounts")
	public void addAmountToAccounts(Integer amount) {
		addMoneyToAccounts(new BigDecimal(amount));
	}

	private void assertHttpStatus(HttpStatus actual, HttpStatus expected) {
		assertThat(actual).isEqualTo(expected);
		previousResponse = actual;
	}

	private void addMoneyToAccounts(BigDecimal amount) {
		getAccounts(header_auth).getBody().forEach(
				account -> transferBetweenAccounts(header_auth, "123", account.getAccountNumber(), "money", amount));

	}

}
