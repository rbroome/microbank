package com.broome.micro.bank.end2endtests.testutils;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.end2endtests.dto.accounts.AccountDTO;
import com.broome.micro.bank.end2endtests.dto.accounts.TransferBetweenAccountsDTO;
import com.broome.micro.bank.end2endtests.dto.cards.BlockCardDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CardBlockedDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CardDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CardPaymentDTO;
import com.broome.micro.bank.end2endtests.dto.cards.ChangePinDTO;
import com.broome.micro.bank.end2endtests.dto.cards.CreateCardDTO;
import com.broome.micro.bank.end2endtests.dto.cards.PinChangedDTO;
import com.broome.micro.bank.end2endtests.dto.transactions.TransactionDTO;
import com.broome.micro.bank.end2endtests.dto.users.SignInDTO;

public class MicrobankApplication {

	private static String ENDPOINT = "http://localhost:8080/";
	private static String CARD = "card/cards";
	private static String ACCOUNT = "account/accounts";
	private static String USERS = "users";
	
	private static String DELETE_ALL_USERS="/testDelete";
	private static String DELETE_ALL_ACCOUNTS="/testDeleteAll";
	String header_auth = null;
	
	
	public void clean() {
		deleteAll();
	}
	
	private void deleteAll() {
		HttpEntity<String> entity = setHeaders(null);
		restTemplate().exchange(UserEndpoint()+DELETE_ALL_USERS, HttpMethod.GET, entity,String.class);
		restTemplate().exchange(AccountEndpoint()+DELETE_ALL_ACCOUNTS, HttpMethod.GET, entity,String.class);

	}

	// signup
	public ResponseEntity<String> signUp(String username, String password) {
		SignInDTO signIn = new SignInDTO();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SignInDTO> entity = new HttpEntity<>(signIn, headers);
		signIn.setUsername(username);
		signIn.setPassword(password);
		ResponseEntity<String> response = restTemplate().exchange(UserEndpoint() + "/sign-up", HttpMethod.POST, entity,
				String.class);
		return response;
	}

	// signin
	public ResponseEntity<String> signIn(String username, String password) {
		SignInDTO signIn = new SignInDTO();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SignInDTO> entity = new HttpEntity<>(signIn, headers);
		signIn.setUsername(username);
		signIn.setPassword(password);
		return restTemplate().exchange(Endpoint() + "login", HttpMethod.POST, entity, String.class);
	}

	// CreateAAccount
	public ResponseEntity<AccountDTO> createAnAccount(String token, String accountName) {
		AccountDTO accountNew = new AccountDTO();
		accountNew.setName(accountName);
		HttpEntity<AccountDTO> entity = setHeaders(accountNew);
		ResponseEntity<AccountDTO> account = restTemplate().exchange(AccountEndpoint(), HttpMethod.POST, entity,
				AccountDTO.class);
		return account;
	}

	// GetAccounts
	public ResponseEntity<List<AccountDTO>> getAccounts(String token) {
		HttpEntity<String> entity = setHeaders(null);
		ResponseEntity<List<AccountDTO>> accounts = restTemplate().exchange(AccountEndpoint(), HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<AccountDTO>>() {
				});

		return accounts;
	}

	// GetAccount
	public ResponseEntity<AccountDTO> getAccount(String token, String accountNumber) {
		HttpEntity<String> entity = setHeaders(null);
		return restTemplate().exchange(AccountEndpoint() + "/" + accountNumber, HttpMethod.GET, entity,
				AccountDTO.class);
	}

	// GetTransactionsForAccount
	public ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(String token, String accountNumber) {
		HttpEntity<String> entity = setHeaders(null);
		return restTemplate().exchange(AccountEndpoint() +"/"+ accountNumber+ "/transactions", HttpMethod.GET, entity,
				new ParameterizedTypeReference<List<TransactionDTO>>() {
				});
	}

	// CreateCardForAccount
	public ResponseEntity<CardDTO> createCard(String token, String pincode, String accountNumber) {
		CreateCardDTO createCard = new CreateCardDTO();
		createCard.setPinCode(pincode);
		HttpEntity<CreateCardDTO> entity = setHeaders(createCard);
		ResponseEntity<CardDTO> response = restTemplate().exchange(
				AccountEndpoint() + "/" + accountNumber + "/cards", HttpMethod.POST, entity, CardDTO.class);
		return response;
	}

	// DoACardPayment
	public ResponseEntity<TransactionDTO> doCardPayment(String token, float amount, String cardNumber, String pincode) {
		CardPaymentDTO payment = new CardPaymentDTO();
		payment.setPinCode(pincode);
		payment.setCardNumber(cardNumber);
		payment.setAmount(new BigDecimal(amount));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<CardPaymentDTO> entity = new HttpEntity<>(payment, headers);
		ResponseEntity<TransactionDTO> response = restTemplate().exchange(CardsEndpoint()+"/payments", HttpMethod.POST, entity,
				TransactionDTO.class);
		return response;

	}

	// ChangeThePINcodeofACard
	public ResponseEntity<PinChangedDTO> changePinCodeForCard(String token, String oldpin, String newpin,
			String cardnumber) {
		ChangePinDTO pinChange = new ChangePinDTO();
		pinChange.setPinCode(oldpin);
		pinChange.setNewPincode(newpin);
		pinChange.setCardNumber(Long.valueOf(cardnumber));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ChangePinDTO> entity = new HttpEntity<>(pinChange, headers);

		ResponseEntity<PinChangedDTO> response = restTemplate().exchange(CardsEndpoint(), HttpMethod.PATCH, entity,
				PinChangedDTO.class);
		return response;

	}

	// BlockACard
	public ResponseEntity<CardDTO> blockCard(String token, String accountnumber,String cardNumber) {
		BlockCardDTO blockCard = new BlockCardDTO();
		blockCard.setBlocked(true);
		HttpEntity<BlockCardDTO> entity = setHeaders(blockCard);

		ResponseEntity<CardDTO> response = restTemplate().exchange(
				AccountEndpoint() + "/" + accountnumber + "/cards/"+cardNumber, HttpMethod.PATCH, entity, CardDTO.class);

		return response;
	}

	// DoATransferBetween2accounts
	public ResponseEntity<TransactionDTO> transferBetweenAccounts(String token,String accountnumber, String toAccount,String message,BigDecimal amount) {
		//Change this
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TransferBetweenAccountsDTO transfer = new TransferBetweenAccountsDTO();
		transfer.setToAccount(toAccount);
		transfer.setMessage(message);
		transfer.setAmount(amount);
		
		HttpEntity<TransferBetweenAccountsDTO> entity = setHeaders(transfer);
		
		ResponseEntity<TransactionDTO> response = restTemplate().exchange(
				AccountEndpoint() + "/" + accountnumber + "/transfer", HttpMethod.POST, entity, TransactionDTO.class);
		
		return response;
	}

	public void setAuthToken(String token) {
		this.header_auth = token;
	}
	
	public String Endpoint() {
		return ENDPOINT;
	}

	public String CardsEndpoint() {
		return ENDPOINT + CARD;
	}

	public String AccountEndpoint() {
		return ENDPOINT + ACCOUNT;
	}

	public String UserEndpoint() {
		return ENDPOINT + USERS;
	}

	RestTemplate restTemplate() {
		RestTemplate restTemplate = new RestTemplate();

		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(0);
		requestFactory.setReadTimeout(0);

		restTemplate.setRequestFactory(requestFactory);
		return restTemplate;
	}

	private <T> HttpEntity<T> setHeaders(T type) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		headers.add("Authorization", header_auth);

		return new HttpEntity<>(type, headers);
	}
	

}
