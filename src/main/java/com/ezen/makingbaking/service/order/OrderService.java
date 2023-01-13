package com.ezen.makingbaking.service.order;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;

public interface OrderService {
	void insertOrder(Order order);
	
	long getNextOrderNo();
	
	void insertOrderItem(List<OrderItem> orderItemList);
	
	Page<CamelHashMap> getOrderList(String userId, Pageable pageable, String orderCondition);
	
	List<CamelHashMap> getOrderContent(String userId);
	
	CamelHashMap getOrderDetail(long orderNo);
	
	List<CamelHashMap> getOrderItem(long orderNo);
	
	Order updateOrder(Order order);

	void updateCancelItemList(List<Map<String, Object>> itemMapList);

	void updateOrderItemSt(List<Map<String, Object>> itemMapList);
}
