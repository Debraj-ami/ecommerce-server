package com.debraj.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.debraj.exception.ProductException;
import com.debraj.model.Product;
import com.debraj.request.CreateProductRequest;

public interface ProductService {
	
	public Product createProduct(CreateProductRequest req);
	
	public String deleteProduct(Long productId)throws ProductException;
	
	public Product updateProduct(Long productId, Product req) throws ProductException;
	
	public Product findProductById(Long id) throws ProductException;
	
    public List<Product> findProductByCategory(String category);
    
    public Page<Product> getAllProduct(String category,List<String>colors,List<String>sizes,Integer minPrice,Integer max, Integer minDiscount,String sort,String stock,Integer pageNumber, Integer pageSize);
    
    List<Product> createMultipleProducts(List<CreateProductRequest> reqs);
    
    

	
}
