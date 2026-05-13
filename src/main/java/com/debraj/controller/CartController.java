package com.debraj.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.debraj.exception.ProductException;
import com.debraj.exception.UserException;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.User;
import com.debraj.request.AddItemRequest;
import com.debraj.service.CartItemService;
import com.debraj.service.CartService;
import com.debraj.service.UserService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final CartItemService cartItemService;
    private final UserService userService;

    public CartController(CartService cartService, CartItemService cartItemService, UserService userService) {
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    // ✅ GET USER CART
    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    // ✅ ADD ITEM TO CART
    @PutMapping("/add")
    public ResponseEntity<String> addItemToCart(
            @RequestBody AddItemRequest req,
            @RequestHeader("Authorization") String jwt) throws ProductException, UserException {

        User user = userService.findUserProfileByJwt(jwt);
        String response = cartService.addCartItem(user.getId(), req);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    // ✅ UPDATE CART ITEM QUANTITY
    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt) throws ProductException {

        CartItem updatedCartItem = null;

        if (cartItem.getQuantity() > 0) {
            updatedCartItem = cartItemService.updateCartItem(cartItemId, cartItem.getQuantity());
        }

        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

    // ✅ REMOVE ITEM FROM CART
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<String> removeItemFromCart(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt) throws ProductException {

        cartItemService.removeCartItem(cartItemId);

        return new ResponseEntity<>("Item removed from cart", HttpStatus.OK);
    }
}