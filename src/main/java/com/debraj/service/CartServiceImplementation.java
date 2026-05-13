package com.debraj.service;

import org.springframework.stereotype.Service;

import com.debraj.exception.ProductException;
import com.debraj.exception.UserException;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.Product;
import com.debraj.model.User;
import com.debraj.repository.CartRepository;
import com.debraj.request.AddItemRequest;

@Service
public class CartServiceImplementation implements CartService {

    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartServiceImplementation(
            CartRepository cartRepository,
            CartItemService cartItemService,
            ProductService productService
    ) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart createCart(User user) {

        Cart cart = new Cart();
        cart.setUser(user);

        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        Product product = productService.findProductById(req.getProductId());

        CartItem isPresent = cartItemService.isCartItemExist(
                cart,
                product,
                req.getSize(),
                userId
        );

        if (isPresent == null) {
            CartItem cartItem = new CartItem();

            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);

            if (cart.getCartItems() != null) {
                cart.getCartItems().add(createdCartItem);
            }
        }

        return "Item Add to Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {

        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            throw new RuntimeException("Cart not found");
        }

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setDiscounte(totalPrice - totalDiscountedPrice); // keep your original method

        return cartRepository.save(cart);
    }

	@Override
	public CartItem addCartItem(String jwt, AddItemRequest req) throws ProductException, UserException {
		// TODO Auto-generated method stub
		return null;
	}
}