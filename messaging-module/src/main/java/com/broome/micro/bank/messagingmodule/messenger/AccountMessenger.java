package com.broome.micro.bank.messagingmodule.messenger;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.messagingmodule.dto.TransactionDTO;

public abstract class AccountMessenger {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountMessenger.class);

	
	public static int authorizeAmount(TransactionDTO transaction) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject map = new JSONObject();
		try {
			map.put("fromAccount", transaction.getFromAccount());
			map.put("toAccount", transaction.getToAccount());
			map.put("amount", transaction.getAmount());
			map.put("message", transaction.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = "http://localhost:8080/accounts/"+transaction.getFromAccount()+"/auth/";
		
		HttpEntity<String> entity = new HttpEntity<String>(map.toString(),headers);
		
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
		LOGGER.info("BODY:"+response);
		return response.getStatusCodeValue();

		
	}

}
