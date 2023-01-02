package com.ezen.makingbaking.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.service.cart.CartService;
import com.ezen.makingbaking.service.order.OrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private CartService cartService;
	
	@GetMapping("/order")
	public ModelAndView orderView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("order/order.html");
		return mv;
	}
	
	@PostMapping("/orderComplete")
	public ModelAndView insertOrder(OrderDTO orderDTO, @AuthenticationPrincipal CustomUserDetails customUser,
			@RequestParam("itemList") String itemList) throws JsonMappingException, JsonProcessingException {
		
		List<Map<String, Object>> itemMapList = new ObjectMapper().readValue(itemList, new TypeReference<List<Map<String, Object>>>() {});
		// 주문번호 생성
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String ym = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String ymd = ym + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		String subNum = "";
		
		for(int i = 1; i <= 6; i++) {
			subNum += (int)(Math.random() * 10);
		}
		
		long orderNo = orderService.getNextOrderNo();
		orderDTO.setOrderNo(orderNo);
		System.out.println(orderNo);
		
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		
		for(int i = 0; i < itemMapList.size(); i++) {
			System.out.println(itemMapList.get(i).toString());
			OrderItem orderItem = OrderItem.builder()
										   .orderNo(orderNo)
										   .itemNo(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()))
										   .orderItemCnt(Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()))
										   .orderItemPrice(Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()) * Integer.parseInt(itemMapList.get(i).get("itemPrice").toString()))
										   .build();
			
			orderItemList.add(orderItem);
		}
		
		
		ModelAndView mv = new ModelAndView();
		
		Order order = Order.builder()
							.userId(customUser.getUsername())
							.orderNo(orderDTO.getOrderNo())
							.orderDate(LocalDateTime.now())
							.orderStatus(orderDTO.getOrderStatus())
							.orderName(orderDTO.getOrderName())
							.orderTel(orderDTO.getOrderTel())
							.shippingAddr1(orderDTO.getShippingAddr1())
							.shippingAddr2(orderDTO.getShippingAddr2())
							.shippingAddr3(orderDTO.getShippingAddr3())
							.orderDeliFee(orderDTO.getOrderDeliFee())
							.orderTotalPrice(orderDTO.getOrderTotalPrice())
							.orderPayment(orderDTO.getOrderPayment())
							.reciName(orderDTO.getReciName())
							.reciTel(orderDTO.getReciTel())
							.orderMail(orderDTO.getOrderMail())
							.orderMessage(orderDTO.getOrderMessage())
							.depositor(orderDTO.getDepositor())
							.orderTotalPayPrice(orderDTO.getOrderTotalPayPrice())
							.build();
		
		orderService.insertOrder(order);
		orderService.insertOrderItem(orderItemList);
		System.out.println("11111111111111");
		
		mv.addObject("totalPayPrice", orderDTO.getOrderTotalPayPrice());
		mv.addObject("orderNo", orderNo);
		
		List<Cart> cartItemList = new ArrayList<Cart>();
		
		for(int i=0; i < itemMapList.size(); i++) {
			Cart cart = Cart.builder()
							.cartNo(Integer.parseInt(itemMapList.get(i).get("cartNo").toString()))
							.itemNo(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()))
							.build();
			
			cartItemList.add(cart);
		}
		
//		cartService.deleteCartItem(cartItemList);
		
		mv.setViewName("order/orderComplete.html");
		return mv;
	}
	
	@GetMapping("/orderComplete")
	public ModelAndView orderCompleteView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("order/order.html");
		return mv;
	}
}
