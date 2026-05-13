package com.debraj.service;

import java.util.List;

import com.debraj.exception.ProductException;
import com.debraj.model.Review;
import com.debraj.model.User;
import com.debraj.request.ReviewRequest;

public interface ReviewService {
	
	public Review createReview(ReviewRequest req,User user) throws ProductException;
	
	public List<Review>getAllReview(Long productId);
	

}
