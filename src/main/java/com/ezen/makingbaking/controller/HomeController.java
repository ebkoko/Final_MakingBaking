package com.ezen.makingbaking.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/home")
public class HomeController {
	@RequestMapping("/main")
	public ModelAndView mainPage() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("main.html");
		
		return mv;
	}
}
