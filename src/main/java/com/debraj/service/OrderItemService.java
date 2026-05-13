package com.debraj.service;

import com.debraj.exception.OrderException;
import com.debraj.model.OrderItem;

public interface OrderItemService {

    public OrderItem createOrderItem(OrderItem orderItem);

    public OrderItem findOrderItemById(Long orderItemId) throws OrderException;
}