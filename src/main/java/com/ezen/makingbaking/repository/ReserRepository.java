package com.ezen.makingbaking.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
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
	
	@Query(value = "SELECT C.*\r\n"
			+ "	, D.FILE_NO\r\n"
			+ "    , D.FILE_NAME\r\n"
			+ "    , D.FILE_ORIGIN_NAME\r\n"
			+ "    , D.FILE_PATH\r\n"
			+ "    FROM(\r\n"
			+ "		SELECT A.*, B.*\r\n"
			+ "			FROM T_MB_RESER A\r\n"
			+ "				, T_MB_DAYCLASS B\r\n"
			+ "			WHERE A.CLASS_NO = B.DAYCLASS_NO\r\n"
			+ "				AND A.USER_ID = :userId\r\n"
			+ "		) C\r\n"
			+ "	LEFT OUTER JOIN T_MB_FILE D\r\n"
			+ "		ON C.DAYCLASS_NO = D.FILE_REFER_NO\r\n"
			+ "        AND D.FILE_NO = 1\r\n"
			+ "        AND D.FILE_TYPE = 'dayclass'\r\n"
			+ "        ORDER BY A.RESER_DATE DESC", nativeQuery=true)
	List<CamelHashMap> findAllReser(@Param("userId") String userId);
	
	@Query(value = "SELECT C.*\r\n"
			+ "	, D.FILE_NO\r\n"
			+ "    , D.FILE_NAME\r\n"
			+ "    , D.FILE_ORIGIN_NAME\r\n"
			+ "    , D.FILE_PATH\r\n"
			+ "    FROM(\r\n"
			+ "		SELECT A.*, B.*,\r\n"
			+ "               CASE \r\n"
			+ "					WHEN DATE_FORMAT(CONCAT(A.PARTI_DATE, ' ', A.PARTI_TIME), '%Y-%m-%d %H-%i-%s') <= NOW()\r\n"
			+ "                    THEN 'I'\r\n"
			+ "                    WHEN DATE_FORMAT(CONCAT(A.PARTI_DATE, ' ', A.PARTI_TIME), '%Y-%m-%d %H-%i-%s') > NOW()\r\n"
			+ "                    THEN 'P'\r\n"
			+ "				END AS CANCEL_ENABLE\r\n"
			+ "			FROM T_MB_RESER A\r\n"
			+ "				, T_MB_DAYCLASS B\r\n"
			+ "			WHERE A.CLASS_NO = B.DAYCLASS_NO\r\n"
			+ "				AND A.RESER_NO = :reserNo\r\n"
			+ "		) C\r\n"
			+ "	LEFT OUTER JOIN T_MB_FILE D\r\n"
			+ "		ON C.DAYCLASS_NO = D.FILE_REFER_NO\r\n"
			+ "        AND D.FILE_NO = 1\r\n"
			+ "        AND D.FILE_TYPE = 'dayclass';", nativeQuery=true)
	CamelHashMap findByReserDetailAndReserNo(@Param("reserNo") long reserNo);
}
