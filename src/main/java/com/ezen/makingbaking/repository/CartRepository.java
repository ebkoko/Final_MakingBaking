package com.ezen.makingbaking.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CartId;

@Transactional
public interface CartRepository extends JpaRepository<Cart, CartId> {
	Optional<Cart> findByItemNoAndUserId(@Param("itemNo") int itemNo, @Param("userId") String userId);
	
	@Query(value = "SELECT A.* FROM T_MB_CART A WHERE A.USER_ID = :userId AND A.CART_STATUS NOT IN ('D', 'O')", nativeQuery = true)
	List<Cart> findByUserIdAndNotInStatus(@Param("userId") String userId);
	
	@Modifying
	@Query(value = "INSERT INTO T_MB_CART(CART_NO, ITEM_NO, USER_ID, CART_ITEM_CNT) VALUES(:#{#iCart.cartNo}, :#{#iCart.itemNo}, :#{#iCart.userId}, 1)", nativeQuery = true)
	void insertNewItem(@Param("iCart") Cart cart);
}
