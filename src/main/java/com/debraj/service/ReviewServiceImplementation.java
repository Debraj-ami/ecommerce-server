package com.debraj.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.model.Product;
import com.debraj.model.Review;
import com.debraj.model.User;
import com.debraj.repository.ProductRepository;
import com.debraj.repository.ReviewRepository;
import com.debraj.request.ReviewRequest;

@Service
public class ReviewServiceImplementation implements ReviewService {
	
	private ReviewRepository reviewRepository;
	private ProductService productService;
	private ProductRepository productRepository;
	
	public ReviewServiceImplementation(ReviewRepository reviewRepository,ProductService productService,ProductRepository productRepository) {
		// TODO Auto-generated constructor stub
		
		this.reviewRepository=reviewRepository;
		this.productService=productService;
	}
	

	@Override
	public Review createReview(ReviewRequest req, User user) throws ProductException {
		// TODO Auto-generated method stub
		
		Product product = productService.findProductById(req.getProductId());
		
		Review review=new Review();
		review.setUser(user);
		review.setProduct(product);
		review.setReview(req.getReview());
		review.setCreatedAt(LocalDateTime.now());
		
		
		return reviewRepository.save(review);
	}

	@Override
	public List<Review> getAllReview(Long productId) {
		// TODO Auto-generated method stub
		return reviewRepository.getAllProductsReview(productId);
	}

}
