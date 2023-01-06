package com.ezen.makingbaking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/mypage")
public class MypageController {
	@GetMapping("/myPage")
	public ModelAndView joinView() {
		ModelAndView mv = new ModelAndView();
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
	public ModelAndView quitView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/quit.html");
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
	
	@GetMapping("/getOrderDetail")
	public ModelAndView orderDetailView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/getOrderDetail.html");
		return mv;
	}
	
	@GetMapping("/getReserDetail")
	public ModelAndView reserDetailView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/getReserDetail.html");
		return mv;
	}
}
