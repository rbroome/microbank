package com.broome.micro.bank.accountservice;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountResponseDTO;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

@Ignore
public class AccountCucumberSteps extends AccountIntegrationTest{
	private static String OK_ACCOUNT="37730001";
	private static String NOK_ACCOUNT="33330001";
	
	@Before
	public void init() {
		super.init();
	}
	
	@Given("user (.+) have an account")
	public void userHaveAnAccount(final String userId) {
		createAccount(userId,"My new account");
	}
	
	@When("user (.+) list accounts")
	public void userListsAccounts(final String userId) {
		
	}
	
	@Then("user (.+) have (\\d+) account")
	public void userHaveAccount(final String userId,final int quantity) {
		ResponseEntity<List<Account>> accounts = getAccountsForUser(userId);
		assertThat(accounts.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(accounts.getBody().size()).isEqualTo(quantity);
	}
	
	@Then("only that account is returned")
	public void onlyCorrectAccountReturned() {
		ResponseEntity<List<Account>> accounts = getAccountsForUser("1");
		assertThat(accounts.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(accounts.getBody().get(0)).isNotNull();
		
		String expected = accounts.getBody().get(0).getAccountNumber().toString();
		
		ResponseEntity<Account> accountResponse = getAccount(expected,"1");
		assertThat(accountResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(accountResponse.getBody()).isNotNull();
		assertThat(accountResponse.getBody().getAccountNumber()).isEqualTo(Long.valueOf(expected));
	}
	@When("user (.+) lists the wrong account and 404 is returned")
	public void listWrongAcount(final String userId) {
		try {
		ResponseEntity<Account> accountResponse = getAccount(NOK_ACCOUNT,userId);
		}catch(HttpClientErrorException exception) {
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
	
	@Then("user (.+) adds an account with name (.+)")
	public void addNewAccountWithName(final String userId, final String accountName) {
		ResponseEntity<Account> response = createAccount(userId, accountName);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
	}
	
	@Then("one account has name (.+)")
	public void oneAccountWithName(final String name) {
		ResponseEntity<List<Account>> accounts = getAccountsForUser("1");
		
		assertThat(accounts.getStatusCode()).isEqualTo(HttpStatus.OK);
		List<Account> accountsWithCorrectName = accounts.getBody().stream()
		.filter(account -> account.getName().equalsIgnoreCase(name))
		.collect(Collectors.toList());
		
		assertThat(accountsWithCorrectName.size()).isNotEqualTo(0);
		assertThat(accountsWithCorrectName.get(0).getName()).isEqualTo(name);
	}
	
	
	
	
	@When("account has balance (\\d+)")
	public void accountHasBalance(final int balance) {
		setBalance(OK_ACCOUNT,balance);
	}
	
	@Then("some service asks if its possible to deduct (\\d+) and it is not allowed")
	public void authorizeAmountNotAllowed(final int amount) {
		ResponseEntity<AuthorizeAmountResponseDTO> auth = authAmount(OK_ACCOUNT,amount);
		
		assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(auth.getBody().isAllowed()).isFalse();
		
	}
	@Then("some service asks if its possible to deduct (\\d+) and it is allowed")
	public void authorizeAmountAllowed(final int amount) {
		ResponseEntity<AuthorizeAmountResponseDTO> auth = authAmount(OK_ACCOUNT,amount);
		
		assertThat(auth.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(auth.getBody().isAllowed()).isTrue();
		
	}
	
	@Then("user (.+) lists user (.+) account and 404 is returned")
	public void listOtherUsersAccount(final String userId,final String accountOwnerId) {
		ResponseEntity<List<Account>> accounts = getAccountsForUser(accountOwnerId);
		assertThat(accounts.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(accounts.getBody().get(0)).isNotNull();
		
		String otherUserAccount = accounts.getBody().get(0).getAccountNumber().toString();
		
		try {
			ResponseEntity<Account> accountResponse = getAccount(otherUserAccount,userId);
		}catch(HttpClientErrorException exception) {
			assertThat(exception.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		}
	}
	
	@When("user (.+) lists the correct account")
	public void listCorrectAcount(final String userId) {}

}
