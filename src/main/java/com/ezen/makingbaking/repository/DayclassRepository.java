package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Dayclass;

public interface DayclassRepository extends JpaRepository<Dayclass, Integer> {
	@Query(value="SELECT A.* \r\n"
			+ "	, B.FILE_NO\r\n"
			+ "	, B.FILE_NAME\r\n"
			+ "    , B.FILE_ORIGIN_NAME\r\n"
			+ "    , B.FILE_PATH\r\n"
			+ "    FROM T_MB_DAYCLASS A\r\n"
			+ "	 	LEFT OUTER JOIN T_MB_FILE B\r\n"
			+ "		 ON A.DAYCLASS_NO = B.FILE_REFER_NO\r\n"
			+ "			AND B.FILE_NO = ("
			+ "								SELECT MIN(FILE_NO)"
			+ "								FROM T_MB_FILE C"
			+ "								WHERE A.DAYCLASS_NO = C.FILE_REFER_NO"
			+ "								AND C.FILE_TYPE = 'class'"
			+ "							)"
			+ "		   AND B.FILE_TYPE = 'class'"
			+ "	   WHERE A.DAYCLASS_NO = :dayclassNo\r\n"
			+ "		 ", nativeQuery=true)
	CamelHashMap findByFileNoAndDayclassNo(@Param("dayclassNo") int dayclassNo);
	
	//관리자 클래스 검색_선민
	Page<Dayclass> findByDayclassNameContaining(String searchKeyword, Pageable pageable); //이름
	Page<Dayclass> findByDayclassTime(char searchKeyword, Pageable pageable); //운영시간
	Page<Dayclass> findByDayclassUseYn(char searchKeyword, Pageable pageable); //진행상태
	Page<Dayclass> findByDayclassNameContainingOrDayclassTimeOrDayclassUseYn(String searchKeyword1, char searchKeyword2, char searchKeyword3, Pageable pageable);
	
	// 관리자 클래스리스트&이미지파일 조인_선민
	@Query(value="SELECT COUNT(*) FROM (SELECT A.*, B.* FROM A.DAYCLASS_NO = B.FILE_NO AND FILE_NO =:fileNo) C", 
			countQuery="" ,nativeQuery=true)
	public Page<CamelHashMap> getDayclassFileList(@Param("fileNo") int fileNo, Pageable pageable);
	
	//데이클래스 이미지
	@Query(value="SELECT A.*"
			+ "		   , B.FILE_NO"
			+ "		   , B.FILE_NAME"
			+ "		   , B.FILE_ORIGIN_NAME"
			+ "		   , B.FILE_PATH"
			+ "		FROM T_MB_DAYCLASS A"
			+ "		LEFT OUTER JOIN T_MB_FILE B"
			+ "		ON A.DAYCLASS_NO = B.FILE_REFER_NO"
			+ "		AND B.FILE_TYPE = 'class'"
			+ "		AND B.FILE_NO = 1",
			countQuery = "SELECT COUNT(*)"
					+ "		FROM ("
					+ "					SELECT A.*"
					+ "						 , B.FILE_NAME"
					+ "						 , B.FILE_ORIGIN_NAME"
					+ "						 , B.FILE_PATH"
					+ "						FROM T_MB_DAYCLASS A"
					+ "						LEFT OUTER JOIN T_MB_FILE B"
					+ "						ON A.DAYCLASS_NO = B.FILE_REFER_NO"
					+ "						AND B.FILE_TYPE = 'class'"
					+ "						AND B.FILE_NO = 1"
					+ "			 ) C",
			nativeQuery = true)
	Page<CamelHashMap> findDayclassAndFile(Pageable pageable);
}
