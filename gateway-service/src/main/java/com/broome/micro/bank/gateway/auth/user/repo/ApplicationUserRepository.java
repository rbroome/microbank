package com.broome.micro.bank.gateway.auth.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.broome.micro.bank.gateway.auth.user.domain.ApplicationUser;



public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long>{
	ApplicationUser findByUsername(String username);

}
