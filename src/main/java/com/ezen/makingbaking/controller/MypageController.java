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
}
