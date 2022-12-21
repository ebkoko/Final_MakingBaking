package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.service.cart.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@GetMapping("/cartList")
	public ModelAndView cartListView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("cart/cartList.html");
		return mv;
	}
}
