package com.debraj.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.model.Product;
import com.debraj.model.Rating;
import com.debraj.model.User;
import com.debraj.repository.RatingRepository;
import com.debraj.request.RatingRequest;

@Service
public class RatingServiceImplementation implements RatingService{
	
	private RatingRepository ratingRepository;
	private ProductService productService;

	public RatingServiceImplementation(RatingRepository ratingRepository,ProductService productService) {
		this.ratingRepository=ratingRepository;
		this.productService=productService;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Rating createRating(RatingRequest req, User user) throws ProductException {
		// TODO Auto-generated method stub
		
		Product product=productService.findProductById(req.getProductId());
		
		Rating rating = new Rating();
		rating.setProduct(product);
		rating.setUser(user);
		rating.setRating(req.getRating());
		rating.setCreatedAt(LocalDateTime.now());
		return ratingRepository.save(rating);
	}

	@Override
	public List<Rating> getProductsRating(Long productId) {
		// TODO Auto-generated method stub
		return ratingRepository.getAllProductsRatings(productId);
	}

}
