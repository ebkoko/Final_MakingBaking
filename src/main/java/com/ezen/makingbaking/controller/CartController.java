package com.ezen.makingbaking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.CartDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.cart.CartService;
import com.ezen.makingbaking.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/cart")
public class CartController {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserService userService;
	
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
	
	@PostMapping("/orderCartList")
	public ModelAndView orderCartList(@RequestParam Map<String, String> paramMap, @AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
		String itemList = paramMap.get("itemList").toString();
		
		User user = customUser.getUser();
		
		List<Map<String, Object>> returnItemList = new ObjectMapper().readValue(itemList, 
																new TypeReference<List<Map<String, Object>>>() {});
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("userInfo", userService.idcheck(user));
		mv.addObject("itemList", returnItemList);
		mv.addObject("totalItemPrice", Integer.parseInt(paramMap.get("totalItemPrice")));
		mv.addObject("deliFee", Integer.parseInt(paramMap.get("deliFee")));
		
		mv.setViewName("/order/order.html");
		return mv;
	}
}
