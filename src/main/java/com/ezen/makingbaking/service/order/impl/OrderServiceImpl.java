package com.ezen.makingbaking.service.order.impl;

import java.util.List;
import java.util.Map;

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

	@Override
	public void updateCancelItemList(List<Map<String, Object>> itemMapList) {
		for(int i = 0; i < itemMapList.size(); i++) {
			System.out.println(itemMapList.get(i).get("itemStatus"));
			
			if(itemMapList.get(i).get("itemStatus").toString().equals("Y")) {
				orderRepository.updateCancelItem(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()),
						 						 Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()),
						 						 itemMapList.get(i).get("itemStatus").toString().charAt(0));
			} else if(itemMapList.get(i).get("itemStatus").toString().equals("S")) {
				itemMapList.get(i).replace("itemStatus", "Y");
				orderRepository.updateCancelItem(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()),
												 Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()),
												 itemMapList.get(i).get("itemStatus").toString().charAt(0));
			}
		}
		
	}

	@Override
	public void updateOrderItemSt(List<Map<String, Object>> itemMapList) {
		// TODO Auto-generated method stub
		for(int i = 0; i < itemMapList.size(); i++) {
			orderRepository.updateOrderItemSt(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()),
											  Integer.parseInt(itemMapList.get(i).get("cartItemCnt").toString()),
											  itemMapList.get(i).get("itemStatus").toString().charAt(0));
		}
	}
}
