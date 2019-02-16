package com.broome.micro.bank.gateway.auth.security.filter;

import static com.broome.micro.bank.gateway.auth.security.config.SecurityConstants.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;
import com.broome.micro.bank.gateway.auth.user.service.BankUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private BankUserDetailsService userService;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager,BankUserDetailsService userService) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			ApplicationUser creds = new ObjectMapper().readValue(request.getInputStream(), ApplicationUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(),
					creds.getPassword(), new ArrayList<>()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		String sub = ((User)authResult.getPrincipal()).getUsername();
		String userId = String.valueOf(userService.getUserIdByName(sub));
		Date expirationDate = new Date(System.currentTimeMillis()+EXPIRATION_TIME);
		
		String token = Jwts.builder()
				.setSubject(sub)
				.claim("userId", userId)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		response.addHeader(HEADER_STRING, TOKEN_PREFIX+token);
		
	}

}