package com.ezen.makingbaking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.CartDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
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
		
		List<CamelHashMap> cartList = cartService.getCartList();
		
		mv.addObject("getCartList", cartList);
		
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
	
	@DeleteMapping("/cart")
	public ResponseEntity<?> deleteCart(CartDTO cartDTO, Model model) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Cart cart = Cart.builder()
							.cartNo(cartDTO.getCartNo())
							.itemNo(cartDTO.getItemNo())
							.build();
			
			cartService.deleteCart(cart);
			
			List<CamelHashMap> cartList = cartService.getCartList();
			response.setItems(cartList);
			
			model.addAttribute("getCartList", cartList);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
}
