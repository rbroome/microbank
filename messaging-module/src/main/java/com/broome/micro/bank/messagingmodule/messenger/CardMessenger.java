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

import com.broome.micro.bank.messagingmodule.dto.CardDTO;

public abstract class CardMessenger {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CardMessenger.class);
	
	
	public static String createCard(CardDTO card) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		JSONObject map = new JSONObject();
		try {
			map.put("userId", card.getUserId());
			map.put("accountNumber", card.getAccountNumber());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		String url = "http://localhost:8000/cards";
		HttpEntity<String> entity = new HttpEntity<String>(map.toString(),headers);
		
		
		ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
		LOGGER.info("Body: " +response.getBody().toString());
		return response.getBody();
	}

}
