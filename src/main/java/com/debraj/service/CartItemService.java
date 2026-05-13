package com.debraj.service;

import com.debraj.exception.ProductException;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.Product;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long cartItemId, int quantity) throws ProductException;

    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);

    public void removeCartItem(Long cartItemId) throws ProductException;

    public CartItem findCartItemById(Long cartItemId) throws ProductException;
}