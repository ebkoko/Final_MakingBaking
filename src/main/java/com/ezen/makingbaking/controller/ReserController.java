package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.OrderItem;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.kakaopay.KakaoPayService;
import com.ezen.makingbaking.service.reser.ReserService;
import com.ezen.makingbaking.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/reser")
public class ReserController {
	@Autowired
	private ReserService reserService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	@Qualifier("kakaoReser")
	private KakaoPayService kakaoPayService;
	
	@GetMapping("/reser")
	public ModelAndView reserView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("reser/reser.html");
		return mv;
	}
	
	@PostMapping("/reserComplete")
	public ModelAndView insertReser(ReserDTO reserDTO, @AuthenticationPrincipal CustomUserDetails customUser,
			Model model) {
		
		if(model.getAttribute("reserNo") != null) {
			reserDTO.setReserNo(Long.parseLong(model.getAttribute("reserNo").toString()));
		}
		
		long reserNo = 0;
		if(reserDTO.getReserNo() == 0) {
			reserNo = reserService.getNextReserNo();
		} else {
			reserNo = reserDTO.getReserNo();
		}
		reserDTO.setReserNo(reserNo);
		
		ModelAndView mv = new ModelAndView();
		
		Reser reser = Reser.builder()
							.userId(customUser.getUsername())
							.reserNo(reserDTO.getReserNo())
							.reserDate(LocalDateTime.now())
							.reserStatus(reserDTO.getReserStatus())
							.partiName(reserDTO.getPartiName())
							.partiTel(reserDTO.getPartiTel())
							.partiTime(reserDTO.getPartiTime())
							.classNo(reserDTO.getClassNo())
							.reserPersonCnt(reserDTO.getReserPersonCnt())
							.orderName(reserDTO.getOrderName())
							.orderTel(reserDTO.getOrderTel())
							.request(reserDTO.getRequest())
							.reserPayment(reserDTO.getReserPayment())
							.depositor(reserDTO.getDepositor())
							.reserTotalPrice(reserDTO.getReserTotalPrice())
							.partiDate(reserDTO.getPartiDate())
							.classPrice(reserDTO.getClassPrice())
							.build();
		
		reserService.insertReser(reser);
		
		mv.addObject("totalPayPrice", reserDTO.getReserTotalPrice());
		mv.addObject("reserNo", reserNo);
		mv.addObject("reserPayment", reserDTO.getReserPayment());
		
		mv.setViewName("reser/reserComplete.html");
		return mv;
	}
	
	@GetMapping("/reserComplete")
	public ModelAndView reserCompleteView(HttpSession session, @AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
		Reser reser = (Reser)session.getAttribute("reser");
		
		
		String className = session.getAttribute("className").toString();
		System.out.println("dayclass=========================================================================" + className);
		//Map<String, Object> dayclassMap = new ObjectMapper().readValue(dayclass, new TypeReference<Map<String, Object>>() {});
		
		long reserNo = reser.getReserNo();
		
		Reser reserClass = new Reser();
				
//		Reser reserDayclass = Reser.builder()
//									   .reserNo(reserNo)
//									   .classNo(Integer.parseInt(dayclassMap.get("classNo").toString()))
//									   .reserPersonCnt(Integer.parseInt(dayclassMap.get("reserPersonCnt").toString()))
//									   .reserTotalPrice(Integer.parseInt(dayclassMap.get("reserPersonCnt").toString()) * Integer.parseInt(dayclassMap.get("classPrice").toString()))
//									   .build();
//		
//		reser.set(reserDayclass);
		
		reser.setReserNo(reserNo);
		reser.setUserId(customUser.getUsername());
		reser.setReserDate(LocalDateTime.now());
		
		System.out.println("reser2======================================================"+reser.toString());
		
		ModelAndView mv = new ModelAndView();
		
//		Order returnOrder = Order.builder()
//							.userId(customUser.getUsername())
//							.orderNo(orderNo)
//							.orderDate(LocalDateTime.now())
//							.orderStatus(order.getOrderStatus())
//							.orderName(order.getOrderName())
//							.orderTel(order.getOrderTel())
//							.shippingAddr1(order.getShippingAddr1())
//							.shippingAddr2(order.getShippingAddr2())
//							.shippingAddr3(order.getShippingAddr3())
//							.orderDeliFee(order.getOrderDeliFee())
//							.orderTotalPrice(order.getOrderTotalPrice())
//							.orderPayment(order.getOrderPayment())
//							.reciName(order.getReciName())
//							.reciTel(order.getReciTel())
//							.orderMail(order.getOrderMail())
//							.orderMessage(order.getOrderMessage())
//							.depositor(order.getDepositor())
//							.orderTotalPayPrice(order.getOrderTotalPayPrice())
//							.build();
//		
		reserService.insertReser(reser);
//		orderService.insertOrderItem(orderItemList);
		
		mv.addObject("totalPayPrice", reser.getReserTotalPrice());
		mv.addObject("reserNo", reserNo);
		mv.addObject("reserPayment", reser.getReserPayment());
		mv.addObject("className", className);
		
//		List<Cart> cartItemList = new ArrayList<Cart>();
//		
//		for(int i=0; i < itemMapList.size(); i++) {
//			Cart cart = Cart.builder()
//							.cartNo(Integer.parseInt(itemMapList.get(i).get("cartNo").toString()))
//							.itemNo(Integer.parseInt(itemMapList.get(i).get("itemNo").toString()))
//							.cartItemCnt(Integer.parseInt(itemMapList.get(i).get("orderItemCnt").toString()))
//							.build();
//			
//			cartItemList.add(cart);
//		}
//		
//		cartService.deleteCartItem(cartItemList);
		
		mv.setViewName("reser/reserComplete.html");
		
		session.removeAttribute("className");
		session.removeAttribute("reser");
		session.removeAttribute("tid");
		
		return mv;
	}
	
	@PostMapping("/pay")
	public ReadyResponseDTO payReady(@RequestParam(name = "total_amount") int totalAmount, Reser reser,
			@RequestParam("className") String className, HttpSession session) throws JsonMappingException, JsonProcessingException {
		// 카카오페이 결제 준비: 결제요청 service 실행
		ReadyResponseDTO readyResponseDTO = kakaoPayService.payReady(totalAmount, className);
		// 요청처리 후 받아온 결제 고유번호(tid)를 모델에 저장
		session.setAttribute("tid", readyResponseDTO.getTid());
		
		reser.setReserNo(readyResponseDTO.getReserNo());
		System.out.println("reser1======================================================"+reser.toString());
		
		// Order 정보를 모델에 저장
		session.setAttribute("reser", reser);
		
		session.setAttribute("className", className);
		
		return readyResponseDTO;
	}
	
	// 결제승인요청
	@GetMapping("/kakaoReserComplete")
	public void payCompleted(@RequestParam("pg_token") String pgToken,
			HttpSession session, HttpServletResponse response) throws IOException {
		long reserNo = ((Reser)session.getAttribute("reser")).getReserNo();
		// 카카오 결재 요청하기
		ApproveResponseDTO approveResponse = kakaoPayService.payApprove(session.getAttribute("tid").toString(), pgToken, reserNo);
		
		response.sendRedirect("/reser/reserComplete");
	}
	
	@GetMapping("/kakaoReserFail")
	public void payFail(HttpServletResponse response) throws IOException {
		response.sendRedirect("/dayclass/{dayclassNo}?msg=fail");
	}
	
	@GetMapping("/kakaoReserCancel")
	public void payCancel(HttpServletResponse response) throws IOException {
		
		response.sendRedirect("/dayclass/reserDayclass?msg=cancel");
	}
}
