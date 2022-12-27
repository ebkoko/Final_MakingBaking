package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.CartDTO;
import com.ezen.makingbaking.entity.Cart;
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
	
	@PostMapping("/insertCart")
	public void insertCart(CartDTO cartDTO) {
		Cart cart = Cart.builder()
						.itemNo(cartDTO.getItemNo())
						.build();
		
		System.out.println(cart.getCartStatus());
		
		cartService.insertCart(cart);
	}
	
//	@PostMapping("/deleteCart")
//	public void deleteCart(@RequestParam("itemNo") int itemNo) {
//		
//	}
}
