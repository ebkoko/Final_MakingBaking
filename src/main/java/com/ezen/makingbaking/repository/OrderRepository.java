package com.ezen.makingbaking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(value="SELECT  IFNULL(MAX(ORDER_NO), CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(0, '6', '0'))) + 1 AS ORDER_NO\r\n"
			+ " FROM T_MB_ORDER\r\n"
			+ " WHERE ORDER_NO LIKE CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '%')", nativeQuery=true)
	long getNextOrderNo();
	
	@Query(value = "SELECT A.*\r\n"
			+ "	FROM T_MB_ORDER A\r\n"
			+ "    WHERE A.USER_ID = :userId\r\n"
			+ "		ORDER BY A.ORDER_NO DESC", nativeQuery=true)
	List<CamelHashMap> findAllOrder(@Param("userId") String userId);
	
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
	
	@Query(value="SELECT A.*\r\n"
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
}
