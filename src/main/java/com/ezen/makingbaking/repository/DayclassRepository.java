package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Dayclass;

public interface DayclassRepository extends JpaRepository<Dayclass, Integer> {
	@Query(value="SELECT B.* \r\n"
			+ "	, A.FILE_NO\r\n"
			+ "	, A.FILE_NAME\r\n"
			+ "    , A.FILE_ORIGIN_NAME\r\n"
			+ "    , A.FILE_PATH\r\n"
			+ "    FROM T_MB_FILE A\r\n"
			+ "		, T_MB_DAYCLASS B\r\n"
			+ "	WHERE B.DAYCLASS_NO = A.FILE_REFER_NO\r\n"
			+ "		AND B.DAYCLASS_NO = :dayclassNo\r\n"
			+ "		AND A.FILE_NO = 1\r\n"
			+ "     AND A.FILE_TYPE = 'class'", nativeQuery=true)
	CamelHashMap findByFileNoAndDayclassNo(@Param("dayclassNo") int dayclassNo);
}
