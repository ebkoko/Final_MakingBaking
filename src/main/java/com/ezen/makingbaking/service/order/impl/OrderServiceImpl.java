package com.ezen.makingbaking.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
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
	
	@Override
	public Page<CamelHashMap> getOrderList(String userId, Pageable pageable, String orderCondition) {
		if(orderCondition == null || orderCondition.equals("") || orderCondition.equals("ALL")) {
			return orderRepository.findAllOrder(userId, pageable);			
		} else {
			return orderRepository.findAllOrderByOrderCondition(userId, orderCondition, pageable);
		}		
	}
	
	@Override
	public List<CamelHashMap> getOrderContent(String userId) {
		return orderRepository.findByOrderContentAndOrderNo(userId);
	}
	
	@Override
	public CamelHashMap getOrderDetail(long orderNo) {
		return orderRepository.findByOrderDetailAndOrderNo(orderNo);
	}
	
	@Override
	public List<CamelHashMap> getOrderItem(long orderNo) {
		return orderRepository.findByItemDetailAndOrderNo(orderNo);
	}
	
	@Override
	public Order updateOrder(Order order) {
		return orderRepository.save(order);
	}
}
