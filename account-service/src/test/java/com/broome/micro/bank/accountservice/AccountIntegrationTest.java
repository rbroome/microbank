package com.broome.micro.bank.accountservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import com.broome.micro.bank.accountservice.domain.Account;
import com.broome.micro.bank.accountservice.dto.AccountDTO;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountDTO;
import com.broome.micro.bank.accountservice.dto.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.accountservice.restclient.TransactionClient;
import com.broome.micro.bank.accountservice.restclient.UserClient;
import com.broome.micro.bank.accountservice.service.AccountService;

import cucumber.api.java.Before;

@Ignore
public class AccountIntegrationTest extends BaseIntegrationTest{
	
	private static final Logger log = LoggerFactory.getLogger(AccountIntegrationTest.class);
	static final String OK_ACCOUNT = "37730001";
	private static String OK_HEADER_AUTH="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6IjEiLCJleHAiOjE1NTExMTcyNDV9.rpnO6mjtxmoOFDzC_yE-D8sgQJZz8BaKEvVSrNwiz3St_Q05Y05z-Ixf0TdtI4Avm9t04oqbWkQxb5r4x19VbQ";
	private static String BAD_HEADER_AUTH="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6IjEiLCJleHAiOjE1N2222yNDV9.rpnO6mjtxmoOFDzC_yE-D8sgQJZz8BaKEvVSrNwiz3St_Q05Y05z-Ixf0TdtI4Avm9t04oqbWkQxb1bQ";
	private static String USER2_HEADER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjIiLCJ1c2VySWQiOiIyIiwiZXhwIjoxNTUxMjYyNjEzfQ.8ow4S69DObyAJK4WGEGgZ5sJT12GbvIwLucftzyAX8XVAn01N3EYx3W1bLi3lCTtB1v0SArlbF8zOku0O93aaQ";
	@Autowired
	protected TransactionClient transactionClient;
	@Autowired
	private AccountService service;
	
	@Autowired
	UserClient userClient;
	
	
	public AccountIntegrationTest() {

	}
	
	public void init() {
		service.removeEverything();
		ResponseEntity<String> resp = ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+OK_HEADER_AUTH)
				.body("");
		when(userClient.login(any())).thenReturn(resp);
	}
	
	public ResponseEntity<Account>createAccount(String userId,String name) {
		AccountDTO accountNew = new AccountDTO();
		accountNew.setName(name);
		HttpEntity<AccountDTO> entity = setHeaders(accountNew, userId);
		ResponseEntity<Account> account = restTemplate().exchange(getEndpoint(), HttpMethod.POST,entity,Account.class);
		return account;
	}
	
	public ResponseEntity<List<Account>> getAccountsForUser(String userId){
		HttpEntity<String> entity = setHeaders(null, userId);
		ResponseEntity<List<Account>> accounts = 
		restTemplate().exchange(getEndpoint(),HttpMethod.GET,entity, new ParameterizedTypeReference<List<Account>>() {
        });
		
		return accounts;
	}
	
	public void setBalance(String accountNumber,int balance) {
		log.info("Looking for account {} with user {}",accountNumber,"1");
		List<Account> accounts= getAccountsForUser("1").getBody();
		
		accounts.forEach(account -> {
			account.setAmount(new BigDecimal(balance));
			service.saveAccount(account);
			
		});		
	};

	public ResponseEntity<Account> getAccount(String accountNumber,String userId) {
		setUpTransactionClient();
		HttpEntity<String> entity = setHeaders(null, userId);
		return restTemplate().exchange(getEndpoint()+"/"+accountNumber,HttpMethod.GET,entity,Account.class);
	}
	private void setUpTransactionClient() {
		when(transactionClient.getTransactions(any(),any())).thenReturn(new ArrayList<>());
	}
	
	public ResponseEntity<AuthorizeAmountResponseDTO> authAmount(String accountnumber,int amount){
		AuthorizeAmountDTO authAmount = new AuthorizeAmountDTO();
		authAmount.setAmount(new BigDecimal(amount));
		List<Account> accounts= getAccountsForUser("1").getBody();
		accountnumber = accounts.get(0).getAccountNumber().toString();
		String url = getEndpoint()+"/"+accountnumber+"/auth";
		log.info("POST to "+url);
		ResponseEntity<AuthorizeAmountResponseDTO> resp = restTemplate().postForEntity(url, authAmount, AuthorizeAmountResponseDTO.class);
		
		return resp;
		
	}
	
	

	
	
	private <T> HttpEntity<T> setHeaders(T type ,String userId){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(userId.equalsIgnoreCase("1")) {
			headers.add("Authorization", "Bearer "+OK_HEADER_AUTH);
		}else if(userId.equalsIgnoreCase("2")) {
			headers.add("Authorization","Bearer "+ USER2_HEADER);
		}
		else {
			headers.add("Authorization", "Bearer "+BAD_HEADER_AUTH);
		}
		return new HttpEntity<>(type,headers);
	}
}
	
	
	