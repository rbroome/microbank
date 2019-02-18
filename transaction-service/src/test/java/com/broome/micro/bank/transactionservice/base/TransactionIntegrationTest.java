package com.broome.micro.bank.transactionservice.base;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.broome.micro.bank.transactionservice.domain.Transaction;
import com.broome.micro.bank.transactionservice.domain.TransactionStatus;
import com.broome.micro.bank.transactionservice.domain.TransactionType;
import com.broome.micro.bank.transactionservice.dto.AuthorizeAmountResponseDTO;
import com.broome.micro.bank.transactionservice.dto.CommitTransactionResponseDTO;
import com.broome.micro.bank.transactionservice.dto.TransactionDTO;
import com.broome.micro.bank.transactionservice.restclient.AccountClient;
import com.broome.micro.bank.transactionservice.restclient.UserClient;
import com.broome.micro.bank.transactionservice.services.TransactionService;

@Ignore
public class TransactionIntegrationTest extends BaseIntegrationTest {

	protected static String SYSTEM_HEADER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzeXNhZG1pbiIsInVzZXJJZCI6IjEiLCJzeXN0ZW0iOnRydWUsImV4cCI6MTU1MTI2ODc1M30.cdxhxVmjWq6-XLp-ixxHTlxNBkanC_Skc27zVEeBR9ri7NRs0pAO_AwGl9VMMghHHyirIlIWfUjojy9miJzpZg";
	protected static String NORMAL_HEADER = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjIiLCJ1c2VySWQiOiIyIiwic3lzdGVtIjpmYWxzZSwiZXhwIjoxNTUxMjY4ODI2fQ.VlfZM0YC94VJSJO9V_OCkYCxBG1k7jpz9VG4KFLUyqj6gHILuoVp10d480nCgsniqAs9z7ucH3pIqBCCxdJuNw";
	protected static String BAD_HEADER_AUTH = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbjIiLCJ1c2VySWQiOiIyIiwic3lzdGVtIjpmYWxzZSwiZXhwIjoxNTUxMjY4ODI2fQ.VlfZM0YC94VJSJO9V_OCkYCxBG1k7jpz9VG4KFLUyqj6gHILuoVp10d480nCgsniqAs9z7ucH3pIqBCCxd";

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	AccountClient client;
	@Autowired
	UserClient userClient;

	
	public void init() {
		transactionService.removeEveything();
		ResponseEntity<String> resp = ResponseEntity.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+SYSTEM_HEADER)
				.body("");
				
				when(userClient.login(any())).thenReturn(resp);
	}

	protected TransactionDTO addTransactionFromService(String accountNumber, String toAccount, int amount, String status) {
		String message = "hello";
		
		autorizeAllTransactions(true);
		return transactionService.addTransactionWithStatus(accountNumber, toAccount, new BigDecimal(amount), message,
				TransactionType.CardTransaction,TransactionStatus.valueOf(status)).getBody();

	}
	
	private void autorizeAllTransactions(boolean isAllowed) {
		AuthorizeAmountResponseDTO auth = new AuthorizeAmountResponseDTO();
		auth.setAllowed(isAllowed);
		when(client.authorizeAmount(any(),any(), any())).thenReturn(auth);
	}
	
	protected ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(String accountNumber, String userId) {
		HttpEntity<String> entity = setHeaders(null, userId);
		ResponseEntity<List<TransactionDTO>> transResponse = 
				restTemplate().exchange(endPoint()+"/"+accountNumber, HttpMethod.GET,entity,new ParameterizedTypeReference<List<TransactionDTO>>() {
				});
		
		return transResponse;
	}
	
	protected ResponseEntity<TransactionDTO> addTransaction(String accountNumber, String toAccount, int amount,String userId,TransactionType type){
		return addTransaction(accountNumber, toAccount, amount, userId, type,true);
		
	}
	
	protected ResponseEntity<TransactionDTO> addTransaction(String accountNumber, String toAccount, int amount,String userId,TransactionType type,boolean allowAll){
		TransactionDTO request = getTransactionDTO(accountNumber, toAccount, amount,type);
		HttpEntity<TransactionDTO> entity = setHeaders(request, userId);
		autorizeAllTransactions(allowAll);
		return restTemplate().exchange(endPoint(), HttpMethod.POST,entity,TransactionDTO.class);
	}
	
	protected ResponseEntity<CommitTransactionResponseDTO> commitTransactions(String accountNumber,String userId){
		HttpEntity<String> entity = setHeaders(null, userId);
		ResponseEntity<CommitTransactionResponseDTO> response =
				restTemplate().exchange(endPoint()+"/"+accountNumber+"/commit",HttpMethod.GET,entity, CommitTransactionResponseDTO.class);
		return response;
	}
	
	private TransactionDTO getTransactionDTO(String accountnumber,String toAccount, int amount,TransactionType type) {
		TransactionDTO dto = new TransactionDTO();
		dto.setFromAccount(accountnumber);
		dto.setToAccount(toAccount);
		dto.setAmount(new BigDecimal(amount));
		dto.setMessage("hello");
		dto.setType(type.toString());
		return dto;
	}
	
	
	private <T> HttpEntity<T> setHeaders(T type ,String userId){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(userId.equalsIgnoreCase("1")) {
			headers.add("Authorization", "Bearer "+SYSTEM_HEADER);
		}else if(userId.equalsIgnoreCase("2")) {
			headers.add("Authorization","Bearer "+ NORMAL_HEADER);
		}
		else {
			headers.add("Authorization", "Bearer "+BAD_HEADER_AUTH);
		}
		return new HttpEntity<>(type,headers);
	}

}
