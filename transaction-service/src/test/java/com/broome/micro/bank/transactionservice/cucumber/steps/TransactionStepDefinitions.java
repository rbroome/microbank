package com.broome.micro.bank.transactionservice.cucumber.steps;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.broome.micro.bank.transactionservice.base.TransactionIntegrationTest;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.dto.CommitTransactionResponseDTO;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Ignore
public class TransactionStepDefinitions extends TransactionIntegrationTest {

	private static final Logger log = LoggerFactory.getLogger(TransactionStepDefinitions.class);

	private static String SYSTEM_USERID = "1";

	@Before
	public void init() {
		init();
	}

	@When("system user asks for transaction for account (.+)")
	public void system_user_asks_for_transaction_for_account(String accountNumber) {
		ResponseEntity<List<TransactionDTO>> response = getTransactionsForAccount(accountNumber, SYSTEM_USERID);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

	}

	@Then("(\\d+) transactions exists for account (.+)")
	public void transactions_exists_for_account(Integer quantity, String accountNumber) {
		ResponseEntity<List<TransactionDTO>> response = getTransactionsForAccount(accountNumber, SYSTEM_USERID);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().size()).isEqualTo(quantity);
	}

	@When("user (.+) asks for transaction for account (.+)")
	public void user_asks_for_transaction_for_account(String userId, String accountNumber) {
		ResponseEntity<List<TransactionDTO>> response = null;
		try {
			response = getTransactionsForAccount(accountNumber, userId);
		} catch (HttpClientErrorException exception) {
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}
		assertThat(response).isNull();
	}

	@Then("status (\\d+) is returned")
	public void status_is_returned(Integer int1) {
	}

	@When("system user creates a transaction between account (.+) and account (.+)")
	public void system_user_creates_a_transaction_between_account_and_account(String accountNumber, String toAccount) {
		ResponseEntity<TransactionDTO> response = addTransaction(accountNumber, toAccount, 123, SYSTEM_USERID,
				TransactionType.CardTransaction);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getFromAccount()).isEqualTo(accountNumber);
		assertThat(response.getBody().getToAccount()).isEqualTo(toAccount);

	}

	@When("system user creates a (.+) type transaction between account (.+) and account (.+)")
	public void system_user_creates_a_CARDPAYMENT_type_transaction_between_account_and_account(String type,
			String accountNumber, String toAccount) {
		ResponseEntity<TransactionDTO> response = addTransaction(accountNumber, toAccount, 123, SYSTEM_USERID,
				TransactionType.valueOf(type));
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getFromAccount()).isEqualTo(accountNumber);
		assertThat(response.getBody().getToAccount()).isEqualTo(toAccount);
	}

	@When("system user creates a transaction between invalid account (.+) and account (.+)")
	public void system_user_creates_a_type_transaction_between_invalid_account_and_account(String accountNumber,
			String toAccount) {
		ResponseEntity<TransactionDTO> response = null;
		try {
			response = addTransaction(accountNumber, toAccount, 123, SYSTEM_USERID, TransactionType.CardTransaction,
					false);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}
		assertThat(response).isNull();

	}

	@Then("(\\d+) transaction exists for account (.+) with status (.+) and type (.+)")
	public void transaction_exists_for_account_with_status_PENDING(Integer quantity, String accountNumber,
			String status, String type) {
		ResponseEntity<List<TransactionDTO>> response = getTransactionsForAccount(accountNumber, SYSTEM_USERID);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		List<TransactionDTO> transactions = response.getBody().stream()
				.filter(transaction -> transaction.getStatus().equalsIgnoreCase(status))
				.filter(transaction -> transaction.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
		assertThat(transactions.size()).isEqualTo(quantity);

		transactions.forEach(transaction -> {
			log.info("Transaction from {} transaction to {} transactionid", transaction.getFromAccount(),
					transaction.getToAccount(), transaction.getTransactionid());
			assertThat(transaction.getStatus()).isEqualToIgnoringCase(status);
			assertThat(transaction.getType()).isEqualTo(type);

		});

	}

	@When("user (.+) creates a transaction between account (.+) and account (.+)")
	public void user_creates_a_transaction_between_account_and_account(String userId, String accountNumber,
			String toAccount) {
		ResponseEntity<TransactionDTO> response = null;
		try {
			response = addTransaction(accountNumber, toAccount, 123, userId, TransactionType.CardTransaction);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}
		assertThat(response).isNull();
	}

	@Given("that (\\d+) transactions exists for account (.+) with status (.+)")
	public void that_transactions_exists_for_account_with_status(Integer quantity, String accountNumber,
			String status) {
		for (int i = 0; i < quantity; i++) {
			addTransactionFromService(accountNumber, "4321", 15, status);
		}
	}

	@When("system user commits transactions for account (.+)")
	public void system_user_commits_transactions_for_account(String accountNumber) {
		ResponseEntity<CommitTransactionResponseDTO> response = commitTransactions(accountNumber, SYSTEM_USERID);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().isSuccess()).isTrue();

	}

	@When("user (.+) commits transactions for account (.+)")
	public void user_commits_transactions_for_account(String userId, String accountNumber) {
		//
		ResponseEntity<CommitTransactionResponseDTO> response = null;
		try {
			response = commitTransactions(accountNumber, userId);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}
		assertThat(response).isNull();
	}

}
