package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
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
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.service.kakaopay.KakaoPayReserCancelService;
import com.ezen.makingbaking.service.kakaopay.KakaoPayService;
import com.ezen.makingbaking.service.reser.ReserService;
import com.ezen.makingbaking.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
	
	@Autowired
	private KakaoPayReserCancelService kakaoPayReserCancelService;
	
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
							.partiStatus(reserDTO.getPartiStatus())
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
		
		String tid = (String)session.getAttribute("tid");
		
		String className = session.getAttribute("className").toString();
		System.out.println("dayclass=========================================================================" + className);
		
		long reserNo = reser.getReserNo();
		
		Reser reserClass = new Reser();
		
		reser.setReserNo(reserNo);
		reser.setUserId(customUser.getUsername());
		reser.setReserDate(LocalDateTime.now());
		reser.setTid(tid);
		
		System.out.println("reser2======================================================"+reser.toString());
		
		ModelAndView mv = new ModelAndView();
		
		reserService.insertReser(reser);
		
		mv.addObject("totalPayPrice", reser.getReserTotalPrice());
		mv.addObject("reserNo", reserNo);
		mv.addObject("reserPayment", reser.getReserPayment());
		mv.addObject("className", className);
		
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
	
	@Transactional
	@PutMapping("/reserCancel/{reserNo}")
	public ResponseEntity<?> reserCancel(@PathVariable("reserNo") long reserNo, ReserDTO reserDTO, 
			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
		try {
			Reser returnReser = Reser.builder()
									.reserNo(reserNo)
									.userId(customUser.getUsername())
									.reserDate(LocalDateTime.parse(reserDTO.getReserDate()))
									.reserStatus(reserDTO.getReserStatus())
									.partiName(reserDTO.getPartiName())
									.partiTel(reserDTO.getPartiTel())
									.partiDate(reserDTO.getPartiDate())
									.partiTime(reserDTO.getPartiTime())
									.classNo(reserDTO.getClassNo())
									.reserPersonCnt(reserDTO.getReserPersonCnt())
									.orderName(reserDTO.getOrderName())
									.orderTel(reserDTO.getOrderTel())
									.request(reserDTO.getRequest())
									.reserPayment(reserDTO.getReserPayment())
									.depositor(reserDTO.getDepositor())
									.reserTotalPrice(reserDTO.getReserTotalPrice())
									.classPrice(reserDTO.getClassPrice())
									.partiStatus(reserDTO.getPartiStatus())
									.reserCancelDate(LocalDateTime.now())
									.build();
			reserService.updateReser(returnReser);
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			
			returnMap.put("getReser", returnReser);
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
//	@Transactional
//	@PutMapping("/payCancel/{reserNo}")
//	public ResponseEntity<?> payCancel(@PathVariable("reserNo") long reserNo, ReserDTO reserDTO,
//			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) {
//		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
//		
//		try {
//			Reser returnReser = Reser.builder()
//									.reserNo(reserNo)
//									.userId(customUser.getUsername())
//									.reserDate(LocalDateTime.parse(reserDTO.getReserDate()))
//									.reserStatus(reserDTO.getReserStatus())
//									.partiName(reserDTO.getPartiName())
//									.partiTel(reserDTO.getPartiTel())
//									.partiDate(reserDTO.getPartiDate())
//									.partiTime(reserDTO.getPartiTime())
//									.classNo(reserDTO.getClassNo())
//									.reserPersonCnt(reserDTO.getReserPersonCnt())
//									.orderName(reserDTO.getOrderName())
//									.orderTel(reserDTO.getOrderTel())
//									.request(reserDTO.getRequest())
//									.reserPayment(reserDTO.getReserPayment())
//									.depositor(reserDTO.getDepositor())
//									.reserTotalPrice(reserDTO.getReserTotalPrice())
//									.classPrice(reserDTO.getClassPrice())
//									.partiStatus(reserDTO.getPartiStatus())
//									.reserCancelDate(LocalDateTime.now())
//									.build();
//			reserService.updateReser(returnReser);
//			
//			Map<String, Object> returnMap = new HashMap<String, Object>();
//			
//			returnMap.put("getReser", returnReser);
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
	public CancelResponseDTO cancelReady(ReserDTO reserDTO,
			HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
//		ResponseDTO<Map<String, Object>> responseDTO = new ResponseDTO<>();
		
//		try {
			Reser returnReser = Reser.builder()
									.reserNo(reserDTO.getReserNo())
									.userId(customUser.getUsername())
									.reserDate(LocalDateTime.parse(reserDTO.getReserDate()))
									.reserStatus("PC")
									.partiName(reserDTO.getPartiName())
									.partiTel(reserDTO.getPartiTel())
									.partiDate(reserDTO.getPartiDate())
									.partiTime(reserDTO.getPartiTime())
									.classNo(reserDTO.getClassNo())
									.reserPersonCnt(reserDTO.getReserPersonCnt())
									.orderName(reserDTO.getOrderName())
									.orderTel(reserDTO.getOrderTel())
									.request(reserDTO.getRequest())
									.reserPayment(reserDTO.getReserPayment())
									.depositor(reserDTO.getDepositor())
									.reserTotalPrice(reserDTO.getReserTotalPrice())
									.classPrice(reserDTO.getClassPrice())
									.partiStatus(reserDTO.getPartiStatus())
									.reserCancelDate(LocalDateTime.now())
									.build();
			
			CancelResponseDTO cancelResponseDTO = kakaoPayReserCancelService.cancelReserReady(reserDTO);
			
			reserService.updateReser(returnReser);
			
			return cancelResponseDTO;
		
	}
}
