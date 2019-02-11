package com.broome.micro.bank.messagingmodule.messenger;

import java.util.List;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.messagingmodule.dto.CardPaymentDTO;
import com.broome.micro.bank.messagingmodule.dto.TransactionDTO;

public abstract class TransactionMessenger {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionMessenger.class);


	
	public static void commitTransactions(String accountNumber) {
		
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		
		String url = "http://localhost:8100/commit/"+accountNumber;
		
		HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		
		
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
		
	}
	
	public static List<TransactionDTO> getTransactions(String accountNumber) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		
		
		String url = "http://localhost:8100/transactions/"+accountNumber;
		
		HttpEntity<String> entity = new HttpEntity<String>(null,headers);
		
		ParameterizedTypeReference a = new ParameterizedTypeReference<List<TransactionDTO>>() {
			
		};
		
		ResponseEntity<List<TransactionDTO>> response = restTemplate.exchange(url, HttpMethod.GET, entity, a);
		LOGGER.info("BODY:"+response);
		
		return response.getBody();

		
	}
	
	public static void addTransaction(CardPaymentDTO payment) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject map = new JSONObject();
		try {
			map.put("fromAccount", payment.getFromAccount());
			map.put("toAccount", "1234");
			map.put("amount", payment.getAmount());
			map.put("message", payment.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "http://localhost:8100/transactions";
		
		HttpEntity<String> entity = new HttpEntity<String>(map.toString(),headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		LOGGER.info("BODY:"+response);

		
	}
}
