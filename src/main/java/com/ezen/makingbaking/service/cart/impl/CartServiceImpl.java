package com.ezen.makingbaking.service.cart.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.repository.CartRepository;
import com.ezen.makingbaking.service.cart.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;
	
	@Override
	public void insertCart(Cart cart) {
		//1. 해당 유저에 아이템 번호로 조회되는 데이터가 있는지 검사
		if(cartRepository.findByItemNoAndUserId(cart.getItemNo(), "aa").isPresent()) {
			cart = cartRepository.findByItemNoAndUserId(cart.getItemNo(), "aa").get();
			
			cart.setCartItemCnt(cart.getCartItemCnt() + 1);

			//3. 있으면 수량 +1
			cartRepository.save(cart);
		} else {
			//2. 없으면 인서트
			if(!CollectionUtils.isEmpty(cartRepository.findByUserIdAndNotInStatus("aa"))) {
				List<Cart> cartList = cartRepository.findByUserIdAndNotInStatus("aa");
				
				cart.setCartNo(cartList.get(0).getCartNo());
				cart.setCartItemCnt(1);
				cart.setUserId("aa");
				
				cartRepository.insertNewItem(cart);
			} else {
				cart.setUserId("aa");
				cart.setCartItemCnt(1);
				cartRepository.save(cart);
			}
		}
		
	}
}
