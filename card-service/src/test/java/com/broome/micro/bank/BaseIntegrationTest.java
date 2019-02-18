package com.broome.micro.bank;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.broome.micro.bank.restclient.TransactionClient;
import com.broome.micro.bank.restclient.UserClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
	private static final Logger logger = LoggerFactory.getLogger(BaseIntegrationTest.class);

	private final String URL = "http://localhost";
	private final String CARD_ENDPOINT = "/cards";

	private RestTemplate restTemplate;
	@LocalServerPort
	protected int port;
	
	@MockBean
	TransactionClient client;
	@MockBean
	UserClient userClient;

	public BaseIntegrationTest() {
			port();
			restTemplate = new RestTemplate();
		}

	protected int port() {
		logger.info("PORT: " + port);
		return port;
	}

}
