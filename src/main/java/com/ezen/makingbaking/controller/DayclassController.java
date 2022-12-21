package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.service.dayclass.DayclassService;

@RestController
@RequestMapping("/dayclass")
public class DayclassController {
	@Autowired
	private DayclassService dayclassService;
	
	
	@GetMapping("/dayclass")
	public ModelAndView dayclassVeiw() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/getDayclass.html");
		return mv;
	}
}
