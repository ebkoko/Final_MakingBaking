package com.ezen.makingbaking.service.cart.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ezen.makingbaking.common.CamelHashMap;
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
		if(cartRepository.findByItemNoAndUserId(cart.getItemNo(), cart.getUserId()).isPresent()) {
			cart = cartRepository.findByItemNoAndUserId(cart.getItemNo(), cart.getUserId()).get();
			
			cart.setCartItemCnt(cart.getCartItemCnt() + 1);
			cart.setCartStatus('C');

			//3. 있으면 수량 +1
			cartRepository.save(cart);
		} else {
			//2. 없으면 인서트
			if(!CollectionUtils.isEmpty(cartRepository.findAllByUserIdAndCartStatus(cart.getUserId(), 'C'))) {
				List<Cart> cartList = cartRepository.findAllByUserIdAndCartStatus(cart.getUserId(), 'C');
				
				cart.setCartNo(cartList.get(0).getCartNo());
				cart.setCartItemCnt(1);
				cart.setUserId(cart.getUserId());
				
				cartRepository.insertNewItem(cart);
			} else {
				cart.setUserId(cart.getUserId());
				cart.setCartItemCnt(1);
				cartRepository.save(cart); 
			}
		}
		
	}
	
	@Override
	public List<CamelHashMap> getCartList(String userId) {
		return cartRepository.findAllItemInfoinCart(userId);
	}
	
	@Override
	public void deleteCart(Cart cart) {
		cartRepository.delete(cart);
	}
	
	@Override
	public void deleteCartItem(List<Cart> cartItemList) {
		for(int i = 0; i < cartItemList.size(); i++) {
			cartRepository.updateCartStatus(cartItemList.get(i));
		}
	}
}
