package com.ezen.makingbaking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	
	//관리자 주문 검색_선민
	Page<Order> findByOrderNo(long searchKeyword, Pageable pageable); //주문번호
	Page<Order> findByUserIdContaining(String searchKeyword, Pageable pageable); //회원아이디
	Page<Order> findByOrderNameContaining(String searchKeyword, Pageable pageable); //주문자명
	Page<Order> findByOrderPaymentContaining(String searchKeyword, Pageable pageable); //결제방법
	Page<Order> findByOrderStatusContaining(String searchKeyword, Pageable pageable); //주문상태
	Page<Order> findByOrderNoOrUserIdContainingOrOrderNameContainingOrOrderPaymentContainingOrOrderStatus(long searchKeyword1, String searchKeyword2, String searchKeyword3, String searchKeyword4, String searchKeyword5, Pageable pageable);
	
	
	@Query(value="SELECT  IFNULL(MAX(ORDER_NO), CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(0, '6', '0'))) + 1 AS ORDER_NO\r\n"
			+ " FROM T_MB_ORDER\r\n"
			+ " WHERE ORDER_NO LIKE CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '%')", nativeQuery=true)
	long getNextOrderNo();
	
	@Query(value = "SELECT A.*\r\n"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "    WHERE A.USER_ID = :userId\r\n"
			+ "		ORDER BY A.ORDER_NO DESC",
			countQuery=" SELECT COUNT(*) FROM ("
					+ "SELECT A.*\r\n"
					+ "	FROM T_MB_ORDER A\r\n"
					+ "    WHERE A.USER_ID = :userId\r\n"
					+ "		ORDER BY A.ORDER_NO DESC"
					+ ") AA", nativeQuery=true)
	Page<CamelHashMap> findAllOrder(@Param("userId") String userId, Pageable pageable);
	
	@Query(value="SELECT B.ORDER_NO\r\n"
			+ "	 , B.ITEM_NO\r\n"
			+ "     , B.ORDER_ITEM_CNT\r\n"
			+ "     , C.ITEM_NAME\r\n"
			+ "     , C.ITEM_PRICE\r\n"
			+ "     , C.ITEM_CATE\r\n"
			+ "     , D.FILE_NAME\r\n"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "	   , T_MB_ORDER_ITEM B\r\n"
			+ "       , T_MB_ITEM C\r\n"
			+ "       , T_MB_FILE D\r\n"
			+ "	WHERE A.USER_ID = :userId\r\n"
			+ "      AND A.ORDER_NO = B.ORDER_NO\r\n"
			+ "      AND B.ITEM_NO = C.ITEM_NO\r\n"
			+ "      AND C.ITEM_NO = D.FILE_REFER_NO\r\n"
			+ "      AND D.FILE_TYPE = 'item'\r\n"
			+ "      AND D.FILE_NO = (\r\n"
			+ "						   SELECT MIN(FILE_NO)\r\n"
			+ "							  FROM T_MB_FILE E\r\n"
			+ "                              WHERE E.FILE_REFER_NO = C.ITEM_NO\r\n"
			+ "                                AND E.FILE_TYPE = 'item'\r\n"
			+ "					  )", nativeQuery=true)
	List<CamelHashMap> findByOrderContentAndOrderNo(@Param("userId") String userId);
	
	@Query(value="SELECT A.*,\r\n"
			+ "               CASE \r\n"
			+ "					WHEN A.ORDER_STATUS = 'DW' OR A.ORDER_STATUS = 'D' OR A.ORDER_STATUS = 'DC' OR A.ORDER_STATUS = 'OC' OR A.ORDER_STATUS = 'PC'\r\n"
			+ "                    THEN 'I'\r\n"
			+ "                    WHEN A.ORDER_STATUS = 'MV' OR A.ORDER_STATUS = 'PE'\r\n"
			+ "                    THEN 'P'\r\n"
			+ "                    ELSE 'I'\r\n"
			+ "				END AS CANCEL_ENABLE"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "    WHERE A.ORDER_NO = :orderNo", nativeQuery=true)
	CamelHashMap findByOrderDetailAndOrderNo(@Param("orderNo") long orderNo);
	
	@Query(value="SELECT B.ORDER_NO\r\n"
			+ "	 , B.ITEM_NO\r\n"
			+ "     , B.ORDER_ITEM_CNT\r\n"
			+ "     , C.ITEM_NAME\r\n"
			+ "     , C.ITEM_PRICE\r\n"
			+ "     , C.ITEM_CATE\r\n"
			+ "     , D.FILE_NAME\r\n"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "	   , T_MB_ORDER_ITEM B\r\n"
			+ "       , T_MB_ITEM C\r\n"
			+ "       , T_MB_FILE D\r\n"
			+ "	WHERE A.ORDER_NO = :orderNo\r\n"
			+ "      AND A.ORDER_NO = B.ORDER_NO\r\n"
			+ "      AND B.ITEM_NO = C.ITEM_NO\r\n"
			+ "      AND C.ITEM_NO = D.FILE_REFER_NO\r\n"
			+ "      AND D.FILE_TYPE = 'item'\r\n"
			+ "      AND D.FILE_NO = (\r\n"
			+ "						   SELECT MIN(FILE_NO)\r\n"
			+ "							  FROM T_MB_FILE E\r\n"
			+ "                              WHERE E.FILE_REFER_NO = C.ITEM_NO\r\n"
			+ "                                AND E.FILE_TYPE = 'item'\r\n"
			+ "					  )", nativeQuery=true)
	List<CamelHashMap> findByItemDetailAndOrderNo(@Param("orderNo") long orderNo);
	
	@Query(value="WITH ONTBL AS (\r\n"
			+ "	SELECT A.*\r\n"
			+ "		 , B.ITEM_NO\r\n"
			+ "		FROM \r\n"
			+ "			T_MB_ORDER A,\r\n"
			+ "			T_MB_ORDER_ITEM B\r\n"
			+ "		WHERE \r\n"
			+ "			A.USER_ID = :userId\r\n"
			+ "		AND B.ITEM_NO = :itemNo\r\n"
			+ "		AND A.ORDER_NO = B.ORDER_NO\r\n"
			+ ")\r\n"
			+ "SELECT ONTBL.*\r\n"
			+ "	 , CASE\r\n"
			+ "		WHEN ONTBL.ORDER_NO IN (\r\n"
			+ "								  SELECT D.ORDER_NO\r\n"
			+ "							   )\r\n"
			+ "		THEN 'Y'\r\n"
			+ "		ELSE 'N'\r\n"
			+ "		END AS REVIEW_YN\r\n"
			+ "	FROM ONTBL\r\n"
			+ "	LEFT OUTER JOIN	 (\r\n"
			+ "						 SELECT E.*\r\n"
			+ "							FROM T_MB_REVIEW E\r\n"
			+ "							WHERE E.RVW_REFER_NO = :itemNo\r\n"
			+ "							  AND E.RVW_TYPE = 'item'\r\n"
			+ "					 ) D\r\n"
			+ "	ON ONTBL.USER_ID = D.RVW_WRITER\r\n"
			+ "	AND ONTBL.ORDER_NO = D.ORDER_NO\r\n"
			+ "	AND ONTBL.ITEM_NO = D.RVW_REFER_NO", nativeQuery=true)
	List<CamelHashMap> getByUserIdAndItemNo(@Param("userId") String loginUserId, @Param("itemNo") int itemNo);
	
	@Query(value = "SELECT A.*\r\n"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "    WHERE A.USER_ID = :userId\r\n"
			+ "			AND A.ORDER_STATUS = :orderCondition"	
			+ "		ORDER BY A.ORDER_NO DESC",
			countQuery=" SELECT COUNT(*) FROM ("
					+ "SELECT A.*\r\n"
					+ "	FROM T_MB_ORDER A\r\n"
					+ "    WHERE A.USER_ID = :userId\r\n"
					+ "			AND A.ORDER_STATUS = :orderCondition"
					+ "		ORDER BY A.ORDER_NO DESC"
					+ ") AA", nativeQuery=true)
	Page<CamelHashMap> findAllOrderByOrderCondition(@Param("userId") String userId, @Param("orderCondition") String orderCondition, Pageable pageable);
}
