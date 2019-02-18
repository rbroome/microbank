package com.broome.micro.bank.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.broome.micro.bank.gateway.auth.user.service.UserDetailsServiceImpl;
import com.broome.micro.bank.gateway.filters.pre.SimpleFilter;

@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
public class GatewayApplication {

  public static void main(String[] args) {
	  ConfigurableApplicationContext context = SpringApplication.run(GatewayApplication.class, args);
	  context.getBean(UserDetailsServiceImpl.class).createSystemUser();
  }

  @Bean
  public SimpleFilter simpleFilter() {
    return new SimpleFilter();
  }
  
  @Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
