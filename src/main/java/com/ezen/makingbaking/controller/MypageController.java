package com.ezen.makingbaking.controller;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.service.dayclass.DayclassService;
import com.ezen.makingbaking.service.order.OrderService;
import com.ezen.makingbaking.service.reser.ReserService;

@RestController
@RequestMapping("/mypage")
public class MypageController {
	@Autowired
	private DayclassService dayclassService;
	
	@Autowired
	private ReserService reserService;
	
	@Autowired
	private OrderService orderService;
	
	@GetMapping("/myPage")
	public ModelAndView myOrderList(OrderDTO orderDTO, @PageableDefault(page = 0, size = 5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ModelAndView mv = new ModelAndView();
		
		Page<CamelHashMap> orderList = orderService.getOrderList(customUser.getUsername(), pageable, orderDTO.getOrderCondition());
		
		List<CamelHashMap> orderContent = orderService.getOrderContent(customUser.getUsername());
		
		mv.addObject("getOrderList", orderList);
		mv.addObject("item", orderContent);
		mv.setViewName("mypage/myPage.html");
		return mv;
	}
	
	@GetMapping("/changeInfo")
	public ModelAndView changeInfoView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/changeInfo.html");
		return mv;
	}
	
	@GetMapping("/quit")
	   public ModelAndView quitView(HttpServletRequest request) {
	      ModelAndView mv = new ModelAndView();
	      mv.setViewName("mypage/quit.html");
	      if(request.getParameter("quitMsg") != null && !request.getParameter("quitMsg").equals("")) {
	         mv.addObject("quitMsg", request.getParameter("quitMsg").toString());
	      }
	      return mv;
	   }
	
	@GetMapping("/newPw")
	public ModelAndView newPwView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/newPw.html");
		return mv;
	}
	
	@GetMapping("/checkPwForm")
	public ModelAndView checkPwFormView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/checkPwForm.html");
		return mv;
	}
	
	@GetMapping("/getOrderDetail/{orderNo}")
	public ModelAndView orderDetailView(@PathVariable long orderNo, @AuthenticationPrincipal CustomUserDetails customUser) {
		CamelHashMap order = orderService.getOrderDetail(orderNo);
		
		List<CamelHashMap> orderContent = orderService.getOrderItem(orderNo);
		
		ModelAndView mv = new ModelAndView();
		
		order.put("order_Date_Str", (Timestamp.valueOf(order.get("orderDate").toString())).toLocalDateTime().toString());
		
		mv.addObject("order", order);
		mv.addObject("item", orderContent);
		mv.setViewName("mypage/getOrderDetail.html");
		return mv;
	}
	
	@GetMapping("/myReserList")
	public ModelAndView myReserList(ReserDTO reserDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ModelAndView mv = new ModelAndView();
		
		Page<CamelHashMap> reserList = reserService.getReserList(customUser.getUsername(), pageable, reserDTO.getReserCondition());
		
		
		mv.addObject("getReserList", reserList);		
		mv.setViewName("mypage/myReserList.html");
		return mv;
	}
	
	@GetMapping("/getReserDetail/{reserNo}")
	public ModelAndView reserDetailView(@PathVariable long reserNo, @AuthenticationPrincipal CustomUserDetails customUser) {
		CamelHashMap reser = reserService.getReserDetail(reserNo);
		
		ModelAndView mv = new ModelAndView();
		
		reser.put("reser_Date_Str", ((Timestamp)reser.get("reserDate")).toLocalDateTime().toString());
		
		mv.addObject("reser", reser);
		mv.setViewName("mypage/getReserDetail.html");
		return mv;
	}
	
	@PostMapping("/myReserListAjax")
	public ResponseEntity<?> myReserListAjax(ReserDTO reserDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Page<CamelHashMap> reserList = reserService.getReserList(customUser.getUsername(), pageable, reserDTO.getReserCondition());
			response.setPageItems(reserList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PostMapping("/myPageAjax")
	public ResponseEntity<?> myPageAjax(OrderDTO orderDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Page<CamelHashMap> orderList = orderService.getOrderList(customUser.getUsername(), pageable, orderDTO.getOrderCondition());
			response.setPageItems(orderList);
			
			for(CamelHashMap c : orderList.getContent()) {
				System.out.println("orderDate =================================" + c.get("orderDate"));
				c.put("order_Date_Str", c.get("orderDate").toString());
			}
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
	}
}
