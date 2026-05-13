package com.debraj.service;

import com.debraj.exception.UserException;
import com.debraj.model.User;

public interface UserService {
	
	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	

}
