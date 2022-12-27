package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.service.reser.ReserService;

@RestController
@RequestMapping("/reser")
public class ReserController {
	@Autowired
	private ReserService reserService;
	
	@GetMapping("/reser")
	public ModelAndView reserView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("reser/reser.html");
		return mv;
	}
	
	@PostMapping("/reserComplete")
	public ModelAndView reserCompleteView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("reser/reserComplete.html");
		return mv;
	}
}
