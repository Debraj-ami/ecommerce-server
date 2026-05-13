package com.debraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debraj.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{
	
	

}
