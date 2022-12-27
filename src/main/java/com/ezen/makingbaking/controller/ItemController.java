package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.service.item.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/list")
	public ModelAndView listView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("list/list.html");
		return mv;
	}
	
	@GetMapping("/item")
	public ModelAndView dayclassVeiw() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");
		return mv;
	}
}
