package com.debraj.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.Product;
import com.debraj.repository.CartItemRepository;
import com.debraj.repository.CartRepository;

@Service
public class CartItemServiceImplementation implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final CartRepository cartRepository;

    public CartItemServiceImplementation(CartItemRepository cartItemRepository,
                                         UserService userService,
                                         CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.cartRepository = cartRepository;
    }

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        CartItem createdCartItem = cartItemRepository.save(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long cartItemId, int quantity) throws ProductException {
        CartItem item = findCartItemById(cartItemId);

        item.setQuantity(quantity);
        item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());

        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        return cartItemRepository.isCartItemExist(cart, product, size, userId);
    }

    @Override
    public void removeCartItem(Long cartItemId) throws ProductException {

        cartItemRepository.deleteById(cartItemId);

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws ProductException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);

        if (opt.isPresent()) {
            return opt.get();
        }

        throw new ProductException("CartItem not found with id : " + cartItemId);
    }
}