package com.ezen.makingbaking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.entity.OrderItemId;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemId> {

}
