package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
