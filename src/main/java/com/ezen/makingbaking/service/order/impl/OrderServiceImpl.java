package com.ezen.makingbaking.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.repository.OrderItemRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Override
	public void insertOrder(Order order) {
		orderRepository.save(order);
	}
	
	@Override
	public long getNextOrderNo() {
		return orderRepository.getNextOrderNo();
	}

	@Override
	public void insertOrderItem(List<OrderItem> orderItemList) {
		orderItemRepository.saveAll(orderItemList);
		
	}
	
	
}
