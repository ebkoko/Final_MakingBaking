package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.service.item.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/list")
	public ModelAndView itemListView(@PageableDefault(page = 0, size = 8) Pageable pageable) {
		Page<CamelHashMap> itemList = itemService.getItemList(pageable);
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("list/list.html");
		mv.addObject("itemList", itemList);
		return mv;
	}
	
	@GetMapping("/item")
	public ModelAndView dayclassVeiw() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");
		return mv;
	}
	
	@GetMapping("/stock")
	public int getItemStock(@RequestParam("itemNo") int itemNo) {
		return itemService.getItemStock(itemNo);
	}
	
	@PostMapping("/list")
	public String list() throws Exception{
		return "/item/list";
	}
}