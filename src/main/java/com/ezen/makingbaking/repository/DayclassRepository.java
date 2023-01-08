package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	//관리자 클래스 검색_선민
	Page<Dayclass> findByDayclassNameContaining(String searchKeyword, Pageable pageable); //이름
	Page<Dayclass> findByDayclassTimeContaining(char searchKeyword, Pageable pageable); //운영시간
	Page<Dayclass> findByDayclassUseYn(char searchKeyword, Pageable pageable); //진행상태
	Page<Dayclass> findByDayclassNameContainingOrDayclassTimeContainingOrDayclassUseYn(String searchKeyword1, char searchKeyword2, char searchKeyword3, Pageable pageable);
	
	// 관리자 클래스리스트&이미지파일 조인_선민
	@Query(value="SELECT COUNT(*) FROM (SELECT A.*, B.* FROM A.DAYCLASS_NO = B.FILE_NO AND FILE_NO =:fileNo) C", 
			countQuery="" ,nativeQuery=true)
	public Page<CamelHashMap> getDayclassFileList(@Param("fileNo") int fileNo, Pageable pageable);
	
}
