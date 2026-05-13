package com.debraj.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.debraj.exception.OrderException;
import com.debraj.model.Address;
import com.debraj.model.Cart;
import com.debraj.model.CartItem;
import com.debraj.model.Order;
import com.debraj.model.OrderItem;
import com.debraj.model.User;
import com.debraj.repository.OrderRepository;

@Service
public class OrderServiceImplementation implements OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final OrderItemService orderItemService;

    public OrderServiceImplementation(OrderRepository orderRepository,
                                      CartService cartService,
                                      OrderItemService orderItemService) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.orderItemService = orderItemService;
    }

    @Override
    public Order createOrder(User user, Address shippingAddress) {

        Cart cart = cartService.findUserCart(user.getId());

        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setShippingAddress(shippingAddress); // auto-saved because of cascade
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setCreateAt(LocalDateTime.now());
        createdOrder.setOrderId("ORD-" + System.currentTimeMillis());
        createdOrder.setOrderStatus("PLACED");
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(cart.getDiscounte());
        createdOrder.setTotalItem(cart.getTotalItem());

        // paymentDetails already initialized in Order class
        createdOrder.getPaymentDetails().setStatus("PENDING");

        // Save order first
        Order savedOrder = orderRepository.save(createdOrder);

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            orderItem.setDeliveryDate(LocalDateTime.now().plusDays(7));

            OrderItem createdOrderItem = orderItemService.createOrderItem(orderItem);
            orderItems.add(createdOrderItem);
        }

        savedOrder.setOrderItems(orderItems);

        // Save again after attaching order items
        savedOrder = orderRepository.save(savedOrder);

        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = orderRepository.findById(orderId);

        if (opt.isPresent()) {
            return opt.get();
        }

        throw new OrderException("Order not found with id : " + orderId);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.getUsersOrders(userId);
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("PLACED");
        return orderRepository.save(order);
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CONFIRMED");
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("SHIPPED");
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("DELIVERED");
        order.setDeliveryDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public Order cancledOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus("CANCELLED");
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.delete(order);
    }
}