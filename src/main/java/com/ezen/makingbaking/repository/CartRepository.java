package com.ezen.makingbaking.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Cart;
import com.ezen.makingbaking.entity.CartId;

@Transactional
public interface CartRepository extends JpaRepository<Cart, CartId> {
	Optional<Cart> findByItemNoAndUserId(@Param("itemNo") int itemNo, @Param("userId") String userId);
	
	@Modifying
	@Query(value = "INSERT INTO T_MB_CART(CART_NO, ITEM_NO, USER_ID, CART_ITEM_CNT) VALUES(:#{#iCart.cartNo}, :#{#iCart.itemNo}, :#{#iCart.userId}, 1)", nativeQuery = true)
	void insertNewItem(@Param("iCart") Cart cart);
	
	List<Cart> findAllByUserIdAndCartStatus(@Param("userId") String userId, @Param("cartStatus") char cartStatus);
	
	@Query(value = "SELECT C.*\r\n"
			+ "	 , D.FILE_NO\r\n"
			+ "     , D.FILE_NAME\r\n"
			+ "     , D.FILE_ORIGIN_NAME\r\n"
			+ "     , D.FILE_PATH\r\n"
			+ "	FROM (\r\n"
			+ "			SELECT A.*\r\n"
			+ "				 , B.ITEM_NAME\r\n"
			+ "				 , B.ITEM_PRICE\r\n"
			+ "				 , B.ITEM_STOCK\r\n"
			+ "				FROM T_MB_CART A\r\n"
			+ "				   , T_MB_ITEM B\r\n"
			+ "				WHERE A.ITEM_NO = B.ITEM_NO\r\n"
			+ "				  AND A.USER_ID = 'aa'\r\n"
			+ "                  AND A.CART_STATUS = 'C'\r\n"
			+ "		 ) C\r\n"
			+ "	LEFT OUTER JOIN T_MB_FILE D\r\n"
			+ "    ON C.ITEM_NO = D.FILE_REFER_NO\r\n"
			+ "    AND D.FILE_NO = 1\r\n"
			+ "    AND D.FILE_TYPE = 'ITEM'", nativeQuery = true)
	List<CamelHashMap> findAllItemInfoinCart(@Param("userId") String userId, @Param("cartStatus") char cartStatus); /*userId 수정*/
}
