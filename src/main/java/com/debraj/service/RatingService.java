package com.debraj.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.model.Rating;
import com.debraj.model.User;
import com.debraj.request.RatingRequest;

@Service
public interface RatingService {
	
	public Rating createRating(RatingRequest req, User user) throws ProductException;
	
	public List<Rating>getProductsRating(Long productId);
	
	

}
