package com.broome.micro.bank.accountservice;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.accountservice.restclient.TransactionClient;
import com.broome.micro.bank.accountservice.service.AccountService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

	private final String URL = "http://localhost";
	private final String ACCOUNT_ENDPOINT = "/accounts";

	private RestTemplate restTemplate;
	@MockBean
	TransactionClient transactionClient;

	@LocalServerPort
	protected int port;

	public BaseIntegrationTest() {
			restTemplate = new RestTemplate();
		}

	protected int port() {
		logger.info("PORT: " + port);
		return port;
	}

	public RestTemplate restTemplate() {
		return restTemplate;
	}
	
	public String getEndpoint() {
		return URL+":"+port()+ACCOUNT_ENDPOINT;
	}
}
