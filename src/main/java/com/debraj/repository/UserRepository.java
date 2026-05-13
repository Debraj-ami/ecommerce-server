package com.debraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debraj.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	public User findByEmail(String email);

}
