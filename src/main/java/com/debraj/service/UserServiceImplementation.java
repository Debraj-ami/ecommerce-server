package com.debraj.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.debraj.config.JwtProvider;
import com.debraj.exception.UserException;
import com.debraj.model.User;
import com.debraj.repository.UserRepository;


@Service
public class UserServiceImplementation implements UserService {

	
	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	
	public UserServiceImplementation(UserRepository userRepository,JwtProvider jwtProvider) {
	
		this.userRepository=userRepository;
		this.jwtProvider=jwtProvider;
		
	}
	@Override
	public User findUserById(Long userId) throws UserException {
		Optional<User>user =userRepository.findById(userId);
		
		if(user.isPresent()) {
			return user.get();
		}
		
		throw new UserException("User not found with id : "+ userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromToken(jwt);
		
		User user=userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("user not found with email"+ email);
		}
		return user;
	}

}
