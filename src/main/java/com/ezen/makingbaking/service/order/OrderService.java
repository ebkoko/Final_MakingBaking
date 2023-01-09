package com.ezen.makingbaking.service.order;

import java.util.List;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;

public interface OrderService {
	void insertOrder(Order order);
	
	long getNextOrderNo();
	
	void insertOrderItem(List<OrderItem> orderItemList);
	
	List<CamelHashMap> getOrderList(String userId);
	
	List<CamelHashMap> getOrderContent(String userId);
	
	CamelHashMap getOrderDetail(long orderNo);
	
	List<CamelHashMap> getOrderItem(long orderNo);
	
	Order updateOrder(Order order);
}
