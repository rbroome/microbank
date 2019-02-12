package com.broome.micro.bank.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broome.micro.bank.domain.Card;

public interface CardRepository extends JpaRepository<Card,Long>{

	Optional<List<Card>> findByUserId(String userId);
	
	Optional<Card> findByUserIdAndCardNumberAndPinCode(String userId,long cardNumber,String pincode);
	Optional<Card> findByCardNumberAndPinCode(long cardNumber,String pincode);
	Optional<Card> findByUserIdAndCardNumberAndAccountNumber(String userId,long cardNumber,String accountNumber);
}
