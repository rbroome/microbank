package com.broome.micro.bank.transactionservice.base;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.transactionservice.restclient.AccountClient;
import com.broome.micro.bank.transactionservice.restclient.UserClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

	private final String URL = "http://localhost";
	private final String TRANSACTION_ENDPOINT = "/transactions";

	private RestTemplate restTemplate;
	@LocalServerPort
	protected int port;
	
	@MockBean
	AccountClient client;
	
	@MockBean
	UserClient userClient;

	public BaseIntegrationTest() {
			restTemplate = new RestTemplate();
			
		}

	protected int port() {
		logger.info("PORT: " + port);
		return port;
	}
	
	RestTemplate restTemplate() {
		return restTemplate;
	}
	
	String endPoint() {
		return URL + ":"+port()+TRANSACTION_ENDPOINT;
	}

}