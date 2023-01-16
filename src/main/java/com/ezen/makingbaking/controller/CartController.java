package com.ezen.makingbaking.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
	public ModelAndView cartListView(@AuthenticationPrincipal CustomUserDetails customUser, HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		
		List<CamelHashMap> cartList = cartService.getCartList(customUser.getUsername());
		
		if(request.getParameter("msg") != null && !request.getParameter("msg").equals(""))
			mv.addObject("orderMsg", request.getParameter("msg"));
		
		mv.addObject("getCartList", cartList);
		
		mv.setViewName("cart/cartList.html");
		return mv;
	}
	
	@PostMapping("/insertCart")
	public void insertCart(CartDTO cartDTO, @AuthenticationPrincipal CustomUserDetails customUser) {
		Cart cart = Cart.builder()
						.itemNo(cartDTO.getItemNo())
						.userId(customUser.getUsername())
						.build();
		
		System.out.println(cart.getCartStatus());
		
		cartService.insertCart(cart);
	}
	
	@DeleteMapping("/cart")
	public ResponseEntity<?> deleteCart(CartDTO cartDTO, Model model, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Cart cart = Cart.builder()
							.cartNo(cartDTO.getCartNo())
							.itemNo(cartDTO.getItemNo())
							.userId(customUser.getUsername())
							.build();
			
			cartService.deleteCart(cart);
			
			List<CamelHashMap> cartList = cartService.getCartList(customUser.getUsername());
			response.setItems(cartList);
			System.out.println(cartList);
			
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
		
		user = userService.idcheck(user);
		
		mv.addObject("userInfo", user);
		mv.addObject("itemList", returnItemList);
		mv.addObject("totalItemPrice", Integer.parseInt(paramMap.get("totalItemPrice")));
		mv.addObject("deliFee", Integer.parseInt(paramMap.get("deliFee")));
		
		mv.setViewName("/order/order.html");
		return mv;
	}
	
	@GetMapping("/orderCartList")
	public ModelAndView orderCartListKakaoCancel(@AuthenticationPrincipal CustomUserDetails customUser, 
			@RequestParam("msg") String msg, HttpSession session) throws JsonMappingException, JsonProcessingException {
		String itemList = session.getAttribute("itemList").toString();
		
		List<Map<String, Object>> returnItemList = new ObjectMapper().readValue(itemList, 
				new TypeReference<List<Map<String, Object>>>() {});
		
		User user = customUser.getUser();
		
		ModelAndView mv = new ModelAndView();
		
		mv.addObject("userInfo", user);
		mv.addObject("itemList", returnItemList);
		mv.addObject("totalItemPrice", Integer.parseInt(returnItemList.get(0).get("totalItemPrice").toString()));
		mv.addObject("deliFee", Integer.parseInt(returnItemList.get(0).get("deliFee").toString()));
		
		mv.setViewName("/order/order.html");
		
		session.removeAttribute("itemList");
		session.removeAttribute("order");
		session.removeAttribute("tid");
		
		return mv;
	}
}
