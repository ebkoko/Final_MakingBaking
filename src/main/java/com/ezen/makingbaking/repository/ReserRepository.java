package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.makingbaking.entity.Reser;

public interface ReserRepository extends JpaRepository<Reser, Integer> {
	@Query(value="SELECT IFNULL(MAX(RESER_NO), CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(0, '6', '0'))) + 1 AS RESER_NO\r\n"
			+ " FROM T_MB_RESER\r\n"
			+ " WHERE RESER_NO LIKE CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '%')", nativeQuery=true)
	long getNextReserNo();
}
