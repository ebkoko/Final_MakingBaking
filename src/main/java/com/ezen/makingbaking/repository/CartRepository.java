package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CartId;

public interface CartRepository extends JpaRepository<Cart, CartId> {

}
