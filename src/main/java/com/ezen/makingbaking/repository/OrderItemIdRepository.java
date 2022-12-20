package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.entity.OrderItemId;

public interface OrderItemIdRepository extends JpaRepository<OrderItem, OrderItemId> {

}
