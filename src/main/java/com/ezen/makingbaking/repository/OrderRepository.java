package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.makingbaking.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Query(value="SELECT  IFNULL(MAX(ORDER_NO), CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(0, '6', '0'))) + 1 AS ORDER_NO\r\n"
			+ " FROM T_MB_ORDER\r\n"
			+ " WHERE ORDER_NO LIKE CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '%')", nativeQuery=true)
	long getNextOrderNo();
}
