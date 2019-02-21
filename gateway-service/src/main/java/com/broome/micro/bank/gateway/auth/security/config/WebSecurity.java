package com.broome.micro.bank.gateway.auth.security.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.broome.micro.bank.gateway.auth.security.filter.JWTAuthenticationFilter;
import com.broome.micro.bank.gateway.auth.security.filter.JWTAuthorizationFilter;
import com.broome.micro.bank.gateway.auth.user.service.BankUserDetailsService;



@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter{

	private BankUserDetailsService userDetailsService;
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public WebSecurity(BankUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
		this.userDetailsService=userDetailsService;
		this.passwordEncoder=passwordEncoder;
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.POST,"/users/sign-up").permitAll()
		.antMatchers(HttpMethod.POST,"/card/cards/payments").permitAll()
		.antMatchers(HttpMethod.PATCH,"/card/cards").permitAll()
		.antMatchers(HttpMethod.GET,"/users/testDelete").permitAll()
		.antMatchers(HttpMethod.GET,"/account/accounts/testDeleteAll").permitAll()
		.anyRequest().authenticated()
		.and()
		.addFilter(new JWTAuthenticationFilter(authenticationManager(),userDetailsService))
		.addFilter(new JWTAuthorizationFilter(authenticationManager()));
		
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
		
	}
	

}