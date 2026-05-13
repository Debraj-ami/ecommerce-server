package com.debraj.service;

import com.debraj.exception.ProductException;
import com.debraj.exception.UserException;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.User;
import com.debraj.request.AddItemRequest;

public interface CartService {
	
	public Cart createCart(User user);
	
	public String addCartItem(Long userId,AddItemRequest req) throws ProductException;
	
	public Cart findUserCart(Long userId);

    public CartItem addCartItem(String jwt, AddItemRequest req) throws ProductException, UserException;
}
		
	


