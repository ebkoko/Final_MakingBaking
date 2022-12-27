package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.service.dayclass.DayclassService;

@RestController
@RequestMapping("/dayclass")
public class DayclassController {
	@Autowired
	private DayclassService dayclassService;
	
	@GetMapping("/onedayClass")
	public ModelAndView onedayClassView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/onedayClass.html");
		return mv;
	}
	
	@GetMapping("/dayclass/{dayclassNo}")
	public ModelAndView getDayclass(@PathVariable int dayclassNo) {
		
		Dayclass dayclass = dayclassService.getDayclass(dayclassNo);
		
		DayclassDTO dayclassDTO = DayclassDTO.builder()
											 .dayclassNo(dayclass.getDayclassNo())
											 .dayclassName(dayclass.getDayclassName())
											 .dayclassPrice(dayclass.getDayclassPrice())
											 .dayclassDetails(dayclass.getDayclassDetails())
											 .dayclassTime(dayclass.getDayclassTime())
											 .dayclassUseYn(dayclass.getDayclassUseYn())
											 .build();
											 
		
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/getDayclass.html");
		
		mv.addObject("dayclass", dayclassService.getDayclass(dayclassNo));
		return mv;
	}
	
	
	
	//
}
