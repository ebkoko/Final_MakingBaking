package com.ezen.makingbaking.service.cart;

import java.util.List;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Cart;

public interface CartService {
	void insertCart(Cart cart);
	
	List<CamelHashMap> getCartList(String userId);
	
	void deleteCart(Cart cart);
	
	void deleteCartItem(List<Cart> cartItemList);
}
