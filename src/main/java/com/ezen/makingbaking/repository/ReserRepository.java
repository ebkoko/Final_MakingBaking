package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.Reser;

public interface ReserRepository extends JpaRepository<Reser, Integer> {
	@Query(value="SELECT IFNULL(MAX(RESER_NO), CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), LPAD(0, '6', '0'))) + 1 AS RESER_NO\r\n"
			+ " FROM T_MB_RESER\r\n"
			+ " WHERE RESER_NO LIKE CONCAT(DATE_FORMAT(NOW(), '%Y%m%d'), '%')", nativeQuery=true)
	long getNextReserNo();
	
	@Query(value="SELECT COUNT(*)"
			+ " FROM T_MB_RESER"
			+ " WHERE CLASS_NO = :#{#reser.classNo}"
			+ " AND PARTI_DATE = :#{#reser.partiDate}"
			+ " AND PARTI_TIME = :#{#reser.partiTime}", nativeQuery=true)
	int getPersonCnt(@Param("reser") Reser reser);
}
