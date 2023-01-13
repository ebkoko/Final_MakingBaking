package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.CancelResponseDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.service.cart.CartService;
import com.ezen.makingbaking.service.kakaopay.KakaoPayOrderCancelService;
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
	@Qualifier("kakaoOrder")
	private KakaoPayService kakaoPayService;
	
	@Autowired
	private KakaoPayOrderCancelService kakaoPayOrderCancelService;
	
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
		orderService.updateOrderItemSt(itemMapList);
		
		mv.setViewName("order/orderComplete.html");
		return mv;
	}
	
	@GetMapping("/orderComplete")
	public ModelAndView orderCompleteView(HttpSession session, @AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
		Order order = (Order)session.getAttribute("order");
		//1. entity, dto에 tid 멤버변수 생성
		//2. 테이블 컬럼에 tid 추가
		//3. 위에서 생성한 order에 세션에 담겨 있는 tid 세팅
		String tid = (String)session.getAttribute("tid");
		
		
		String itemList = session.getAttribute("itemList").toString();
		System.out.println("itemList=========================================================================" + itemList);
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
		
		order.setOrderNo(orderNo);
		order.setUserId(customUser.getUsername());
		order.setOrderDate(LocalDateTime.now());
		order.setOrderTotalPayPrice(order.getOrderTotalPrice() + order.getOrderDeliFee());
		order.setTid(tid);
		
		System.out.println("order2======================================================"+order.toString());
		
		ModelAndView mv = new ModelAndView();
		
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
		orderService.updateOrderItemSt(itemMapList);
		
		mv.setViewName("order/orderComplete.html");
		
		session.removeAttribute("itemList");
		session.removeAttribute("order");
		session.removeAttribute("tid");
		
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
		System.out.println("order1======================================================"+order.toString());
		
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
	
	@Transactional
	@PutMapping("/orderCancel/{orderNo}")
	public ResponseEntity<?> orderCancel(@PathVariable("orderNo") long orderNo, OrderDTO orderDTO,
			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		try {
			Order returnOrder = Order.builder()
									.orderNo(orderNo)
									.userId(customUser.getUsername())
									.orderDate(LocalDateTime.parse(orderDTO.getOrderDate()))
									.orderStatus(orderDTO.getOrderStatus())
									.reciName(orderDTO.getReciName())
									.reciTel(orderDTO.getReciTel())
									.shippingAddr1(orderDTO.getShippingAddr1())
									.shippingAddr2(orderDTO.getShippingAddr2())
									.shippingAddr3(orderDTO.getShippingAddr3())
									.orderDeliFee(orderDTO.getOrderDeliFee())
									.orderTotalPrice(orderDTO.getOrderTotalPrice())
									.orderPayment(orderDTO.getOrderPayment())
									.orderName(orderDTO.getOrderName())
									.orderTel(orderDTO.getOrderTel())
									.orderMessage(orderDTO.getOrderMessage())
									.orderMail(orderDTO.getOrderMail())
									.depositor(orderDTO.getDepositor())
									.orderTotalPayPrice(orderDTO.getOrderTotalPayPrice())
									.orderCancelDate(LocalDateTime.now())
									.build();
			orderService.updateOrder(returnOrder);
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("getOrder", returnOrder);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
//	@Transactional
//	@PutMapping("/payCancel/{orderNo}")
//	public ResponseEntity<?> payCancel(@PathVariable("orderNo") long orderNo, OrderDTO orderDTO,
//			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) {
//		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
//		
//		try {
//			Order returnOrder = Order.builder()
//									.orderNo(orderNo)
//									.userId(customUser.getUsername())
//									.orderDate(LocalDateTime.parse(orderDTO.getOrderDate()))
//									.orderStatus(orderDTO.getOrderStatus())
//									.reciName(orderDTO.getReciName())
//									.reciTel(orderDTO.getReciTel())
//									.shippingAddr1(orderDTO.getShippingAddr1())
//									.shippingAddr2(orderDTO.getShippingAddr2())
//									.shippingAddr3(orderDTO.getShippingAddr3())
//									.orderDeliFee(orderDTO.getOrderDeliFee())
//									.orderTotalPrice(orderDTO.getOrderTotalPrice())
//									.orderPayment(orderDTO.getOrderPayment())
//									.orderName(orderDTO.getOrderName())
//									.orderTel(orderDTO.getOrderTel())
//									.orderMessage(orderDTO.getOrderMessage())
//									.orderMail(orderDTO.getOrderMail())
//									.depositor(orderDTO.getDepositor())
//									.orderTotalPayPrice(orderDTO.getOrderTotalPayPrice())
//									.orderCancelDate(LocalDateTime.now())
//									.build();
//			orderService.updateOrder(returnOrder);
//			
//			Map<String, Object> returnMap = new HashMap<String, Object>();
//			
//			returnMap.put("getOrder", returnOrder);
//			
//			responseDTO.setItem(returnMap);
//			
//			return ResponseEntity.ok().body(responseDTO);
//		} catch(Exception e) {
//			responseDTO.setErrorMessage(e.getMessage());
//			
//			return ResponseEntity.badRequest().body(responseDTO);
//		}
//	}
	
	@PostMapping("/payCancel")
	public CancelResponseDTO cancelReady(
			OrderDTO orderDTO, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser,
			@RequestParam("itemList") String itemList, HttpSession session) throws JsonMappingException, JsonProcessingException {
		
//		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
//		
//		try {
			Order returnOrder = Order.builder()
									.orderNo(orderDTO.getOrderNo())
									.userId(customUser.getUsername())
									.orderDate(LocalDateTime.parse(orderDTO.getOrderDate()))
									.orderStatus("PC")
									.reciName(orderDTO.getReciName())
									.reciTel(orderDTO.getReciTel())
									.shippingAddr1(orderDTO.getShippingAddr1())
									.shippingAddr2(orderDTO.getShippingAddr2())
									.shippingAddr3(orderDTO.getShippingAddr3())
									.orderDeliFee(orderDTO.getOrderDeliFee())
									.orderTotalPrice(orderDTO.getOrderTotalPrice())
									.orderPayment(orderDTO.getOrderPayment())
									.orderName(orderDTO.getOrderName())
									.orderTel(orderDTO.getOrderTel())
									.orderMessage(orderDTO.getOrderMessage())
									.orderMail(orderDTO.getOrderMail())
									.depositor(orderDTO.getDepositor())
									.orderTotalPayPrice(orderDTO.getOrderTotalPayPrice())
									.orderCancelDate(LocalDateTime.now())
									.build();

			// 카카오페이 결제취소 준비: 결제취소요청 service 실행
			CancelResponseDTO cancelResponseDTO = kakaoPayOrderCancelService.cancelReady(orderDTO);
			
			
			// Order 정보를 모델에 저장
			orderService.updateOrder(returnOrder);
			
			List<Map<String, Object>> itemMapList = new ObjectMapper().readValue(itemList, new TypeReference<List<Map<String, Object>>>() {});
			
			orderService.updateCancelItemList(itemMapList);
				
			return cancelResponseDTO;
			
		
		
	}
}
