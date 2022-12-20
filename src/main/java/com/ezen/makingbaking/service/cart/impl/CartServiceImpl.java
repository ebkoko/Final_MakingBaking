package com.ezen.makingbaking.service.cart.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.repository.CartRepository;
import com.ezen.makingbaking.service.cart.CartService;

@Service
public class CartServiceImpl implements CartService {
	@Autowired
	private CartRepository cartRepository;
}
