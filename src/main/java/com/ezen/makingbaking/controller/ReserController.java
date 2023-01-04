package com.ezen.makingbaking.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.reser.ReserService;
import com.ezen.makingbaking.service.user.UserService;

@RestController
@RequestMapping("/reser")
public class ReserController {
	@Autowired
	private ReserService reserService;
	
	@Autowired
	private UserService userService;
	
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
							.build();
		
		reserService.insertReser(reser);
		
		mv.addObject("totalPayPrice", reserDTO.getReserTotalPrice());
		mv.addObject("reserNo", reserNo);
		mv.addObject("reserPayment", reserDTO.getReserPayment());
		
		mv.setViewName("reser/reserComplete.html");
		return mv;
	}
}
