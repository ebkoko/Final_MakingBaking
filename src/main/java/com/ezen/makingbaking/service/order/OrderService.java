package com.ezen.makingbaking.service.order;

import java.util.List;

import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;

public interface OrderService {
	void insertOrder(Order order);
	
	long getNextOrderNo();
	
	void insertOrderItem(List<OrderItem> orderItemList);
}
