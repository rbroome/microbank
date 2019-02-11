package com.broome.micro.bank.accountservice.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broome.micro.bank.accountservice.domain.Account;

public interface AccountRepository extends JpaRepository<Account,Long>{
	
	Optional<Account> findByAccountNumber(String accountNumber);
	
	Optional<List<Account>> findByUserId(Long userId);
	
	Optional<Account> findByUserIdAndAccountNumber(Long userId,Long accountNumber);

}
