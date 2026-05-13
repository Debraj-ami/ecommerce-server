package com.debraj.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.debraj.exception.OrderException;
import com.debraj.exception.UserException;
import com.debraj.model.Address;
import com.debraj.model.Order;
import com.debraj.model.User;
import com.debraj.service.OrderService;
import com.debraj.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // ✅ CREATE ORDER
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody Address shippingAddress,
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        Order createdOrder = orderService.createOrder(user, shippingAddress);

        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    // ✅ GET ORDER BY ID
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> findOrderById(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt); // only to validate JWT
        Order order = orderService.findOrderById(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ GET USER ORDER HISTORY
    @GetMapping("/user")
    public ResponseEntity<List<Order>> usersOrderHistory(
            @RequestHeader("Authorization") String jwt) throws UserException {

        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // ✅ PLACE ORDER
    @PutMapping("/{orderId}/place")
    public ResponseEntity<Order> placedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        Order order = orderService.placedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ CONFIRM ORDER
    @PutMapping("/{orderId}/confirm")
    public ResponseEntity<Order> confirmedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        Order order = orderService.confirmedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ SHIP ORDER
    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        Order order = orderService.shippedOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ DELIVER ORDER
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliveredOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        Order order = orderService.deliveredOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ CANCEL ORDER
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancledOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        Order order = orderService.cancledOrder(orderId);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    // ✅ GET ALL ORDERS (ADMIN)
    @GetMapping("/admin")
    public ResponseEntity<List<Order>> getAllOrders(
            @RequestHeader("Authorization") String jwt) throws UserException {

        userService.findUserProfileByJwt(jwt); // optional: only validates JWT
        List<Order> orders = orderService.getAllOrders();

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // ✅ DELETE ORDER (ADMIN)
    @DeleteMapping("/admin/{orderId}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt) throws UserException, OrderException {

        userService.findUserProfileByJwt(jwt);
        orderService.deleteOrder(orderId);

        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }
}