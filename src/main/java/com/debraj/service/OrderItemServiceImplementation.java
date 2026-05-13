package com.debraj.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.debraj.exception.OrderException;
import com.debraj.model.OrderItem;
import com.debraj.repository.OrderItemRepository;

@Service
public class OrderItemServiceImplementation implements OrderItemService {

	@Autowired
    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImplementation(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem findOrderItemById(Long orderItemId) throws OrderException {
        Optional<OrderItem> opt = orderItemRepository.findById(orderItemId);

        if (opt.isPresent()) {
            return opt.get();
        }

        throw new OrderException("Order item not found with id : " + orderItemId);
    }
}