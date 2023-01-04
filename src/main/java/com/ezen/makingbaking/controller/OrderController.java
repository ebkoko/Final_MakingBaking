package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.service.cart.CartService;
import com.ezen.makingbaking.service.kakaopay.KakaoPayService;
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
	
	@Autowired
	private KakaoPayService kakaoPayService;
	
	@GetMapping("/order")
	public ModelAndView orderView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("order/order.html");
		return mv;
	}
	
	@PostMapping("/orderComplete")
	public ModelAndView insertOrder(OrderDTO orderDTO, @AuthenticationPrincipal CustomUserDetails customUser,
			@RequestParam("itemList") String itemList, Model model) throws JsonMappingException, JsonProcessingException {
		List<Map<String, Object>> itemMapList = new ObjectMapper().readValue(itemList, new TypeReference<List<Map<String, Object>>>() {});
			
	
		long orderNo = orderService.getNextOrderNo();
		orderDTO.setOrderNo(orderNo);
		
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
		
		mv.addObject("totalPayPrice", orderDTO.getOrderTotalPayPrice());
		mv.addObject("orderNo", orderNo);
		mv.addObject("orderPayment", orderDTO.getOrderPayment());
		
		List<Cart> cartItemList = new ArrayList<Cart>();
		
		for(int i=0; i < itemMapList.size(); i++) {
			Cart cart = Cart.builder()
							.cartNo(Integer.parseInt(itemMapList.get(i).get("cartNo").toString()))
							.itemNo(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()))
							.cartItemCnt(Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()))
							.build();
			
			cartItemList.add(cart);
		}
		
		cartService.deleteCartItem(cartItemList);
		
		mv.setViewName("order/orderComplete.html");
		return mv;
	}
	
	@GetMapping("/orderComplete")
	public ModelAndView orderCompleteView(HttpSession session, @AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
		Order order = (Order)session.getAttribute("order");
		String itemList = session.getAttribute("itemList").toString();
		
		List<Map<String, Object>> itemMapList = new ObjectMapper().readValue(itemList, new TypeReference<List<Map<String, Object>>>() {});
		
		long orderNo = order.getOrderNo();
		
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
		
		Order returnOrder = Order.builder()
							.userId(customUser.getUsername())
							.orderNo(orderNo)
							.orderDate(LocalDateTime.now())
							.orderStatus(order.getOrderStatus())
							.orderName(order.getOrderName())
							.orderTel(order.getOrderTel())
							.shippingAddr1(order.getShippingAddr1())
							.shippingAddr2(order.getShippingAddr2())
							.shippingAddr3(order.getShippingAddr3())
							.orderDeliFee(order.getOrderDeliFee())
							.orderTotalPrice(order.getOrderTotalPrice())
							.orderPayment(order.getOrderPayment())
							.reciName(order.getReciName())
							.reciTel(order.getReciTel())
							.orderMail(order.getOrderMail())
							.orderMessage(order.getOrderMessage())
							.depositor(order.getDepositor())
							.orderTotalPayPrice(order.getOrderTotalPayPrice())
							.build();
		
		orderService.insertOrder(order);
		orderService.insertOrderItem(orderItemList);
		
		mv.addObject("totalPayPrice", order.getOrderTotalPayPrice());
		mv.addObject("orderNo", orderNo);
		mv.addObject("orderPayment", order.getOrderPayment());
		
		List<Cart> cartItemList = new ArrayList<Cart>();
		
		for(int i=0; i < itemMapList.size(); i++) {
			Cart cart = Cart.builder()
							.cartNo(Integer.parseInt(itemMapList.get(i).get("cartNo").toString()))
							.itemNo(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()))
							.cartItemCnt(Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()))
							.build();
			
			cartItemList.add(cart);
		}
		
		cartService.deleteCartItem(cartItemList);
		
		mv.setViewName("order/orderComplete.html");
		return mv;
	}
	
	@PostMapping("/pay")
	public ReadyResponseDTO payReady(@RequestParam(name = "total_amount") int totalAmount, Order order,
			@RequestParam("itemList") String itemList, HttpSession session) throws JsonMappingException, JsonProcessingException {
		// 카카오페이 결제 준비: 결제요청 service 실행
		ReadyResponseDTO readyResponseDTO = kakaoPayService.payReady(totalAmount, itemList);
		// 요청처리 후 받아온 결제 고유번호(tid)를 모델에 저장
		session.setAttribute("tid", readyResponseDTO.getTid());
		
		order.setOrderNo(readyResponseDTO.getOrderNo());
		
		// Order 정보를 모델에 저장
		session.setAttribute("order", order);
		
		session.setAttribute("itemList", itemList);
		
		return readyResponseDTO;
	}
	
	// 결제승인요청
	@GetMapping("/kakaoOrderComplete")
	public void payCompleted(@RequestParam("pg_token") String pgToken,
			HttpSession session, HttpServletResponse response) throws IOException {
		long orderNo = ((Order)session.getAttribute("order")).getOrderNo();
		// 카카오 결재 요청하기
		ApproveResponseDTO approveResponse = kakaoPayService.payApprove(session.getAttribute("tid").toString(), pgToken, orderNo);
		
		response.sendRedirect("/order/orderComplete");
	}
	
	@GetMapping("/kakaoOrderFail")
	public void payFail(HttpServletResponse response) throws IOException {
		response.sendRedirect("/cart/cartList?msg=fail");
	}
	
	@GetMapping("/kakaoOrderCancel")
	public void payCancel(HttpServletResponse response) throws IOException {
		
		response.sendRedirect("/cart/orderCartList?msg=cancel");
	}
}
