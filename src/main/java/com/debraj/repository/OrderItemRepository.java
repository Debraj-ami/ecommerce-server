package com.debraj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.debraj.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}