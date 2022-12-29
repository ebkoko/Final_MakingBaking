package com.ezen.makingbaking.service.order.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.repository.OrderItemIdRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.service.order.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemIdRepository orderItemIdRepository;
	
//	@Override
//	public List<CamelHashMap> getOrderList(/*@AuthenticationPricipal CustomUserDetails customUser*/) {
//		return orderItemIdRepository.findAllItemInfoinOrder("aa", 'C');
//	}
}
