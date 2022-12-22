package com.ezen.makingbaking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("main")
public class MainController {
	@GetMapping("main")
	public ModelAndView mainView(@RequestParam("msg") String msg) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/main.html");
		
		if(msg != null && !msg.equals("")) {
			if(msg.equals("joinSuccess")) {
				mv.addObject("msg", "joinSuccess");
			}
		}
		return mv;
	}
}
