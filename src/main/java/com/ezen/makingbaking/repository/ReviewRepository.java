package com.ezen.makingbaking.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Review;

@Transactional
public interface ReviewRepository extends JpaRepository<Review, Integer> {
	//Page<Review> findByRvwReferNo (@Param("rvwReferNo") int rvwReferNo, Pageable pageable);
	
	Page<Review> findByRvwReferNoAndRvwType (@Param("rvwReferNo") int rvwReferNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//최신순
	Page<Review> findByRvwReferNoAndRvwTypeOrderByRvwRegdateDesc (@Param("rvwReferNo") int rvwReferNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//오래된순
	Page<Review> findByRvwReferNoAndRvwTypeOrderByRvwRegdateAsc (@Param("rvwReferNo") int rvwReferNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//평점높은순 
	Page<Review> findByRvwReferNoAndRvwTypeOrderByRvwScoreDesc (@Param("rvwReferNo") int rvwReferNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//평점낮은순
	Page<Review> findByRvwReferNoAndRvwTypeOrderByRvwScoreAsc (@Param("rvwReferNo") int rvwReferNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//Page<Review> findByReferNoAndRvwType (@Param("rvwReferNo") int itemNo, @Param("rvwType") String rvwType, Pageable pageable);
	
	//개인 리뷰리스트_선민
	@Query(value="SELECT * FROM T_MB_REVIEW"
			+ " WHERE RVW_WRITER = :rvwWriter", nativeQuery=true)
	Page<Review> getUserRvwList(@Param("rvwWriter") String rvwWriter, Pageable pageable);

	//개인 리뷰팝업_선민
	Page<Review> findByRvwTypeAndRvwWriter(@Param("rvwType") String rvwType, @Param("rvwWriter") String rvwWriter, Pageable pageable); //item, class
	Page<Review> findByRvwWriter(@Param("rvwWriter") String rvwWriter, Pageable pageable);
	
	//관리자 리뷰 검색_선민
	Page<Review> findByRvwWriterContaining(String searchKeyword, Pageable pageable); //리뷰작성자
	Page<Review> findByRvwTypeContaining(String searchKeyword, Pageable pageable); //리뷰타입
	Page<Review> findByRvwWriterContainingOrRvwTypeContaining(String searchKeyword1, String searchKeyword2, Pageable pageable);

	// 마이페이지 리뷰리스트
	@Query(value="SELECT F.*\r\n"
			+ "	 , D.FILE_NO\r\n"
			+ "     , D.FILE_NAME\r\n"
			+ "     , D.FILE_ORIGIN_NAME\r\n"
			+ "     , D.FILE_PATH\r\n"
			+ "	FROM (\r\n"
			+ "			SELECT A.*\r\n"
			+ "				 , CASE\r\n"
			+ "					WHEN A.RVW_TYPE = 'item' AND A.RVW_REFER_NO = B.ITEM_NO\r\n"
			+ "					THEN B.ITEM_NAME\r\n"
			+ "					WHEN A.RVW_TYPE = 'class' AND A.RVW_REFER_NO = C.DAYCLASS_NO\r\n"
			+ "					THEN C.DAYCLASS_NAME\r\n"
			+ "				   END AS RVW_REFER_NAME\r\n"
			+ "				FROM T_MB_REVIEW A\r\n"
			+ "				   , T_MB_ITEM B\r\n"
			+ "				   , T_MB_DAYCLASS C\r\n"
			+ "				WHERE A.RVW_REFER_NO = B.ITEM_NO\r\n"
			+ "				  AND A.RVW_REFER_NO = C.DAYCLASS_NO\r\n"
			+ "				  AND A.RVW_WRITER = :userId\r\n"
			+ "          ) F\r\n"
			+ "	LEFT OUTER JOIN T_MB_FILE D\r\n"
			+ "		ON F.RVW_TYPE = D.FILE_TYPE\r\n"
			+ "	   AND F.RVW_REFER_NO = D.FILE_REFER_NO\r\n"
			+ "       AND D.FILE_NO = (\r\n"
			+ "							SELECT MIN(E.FILE_NO)\r\n"
			+ "								FROM T_MB_FILE E\r\n"
			+ "                                WHERE D.FILE_REFER_NO = E.FILE_REFER_NO\r\n"
			+ "                                  AND D.FILE_TYPE = E.FILE_TYPE\r\n"
			+ "						)\r\n"
			, countQuery="SELECT COUNT(*)"
					+ "		FROM ("
					+ "				SELECT F.*\r\n"
					+ "	 				, D.FILE_NO\r\n"
					+ "     			, D.FILE_NAME\r\n"
					+ "     			, D.FILE_ORIGIN_NAME\r\n"
					+ "     			, D.FILE_PATH\r\n"
					+ "				FROM (\r\n"
					+ "						SELECT A.*\r\n"
					+ "				 		, CASE\r\n"
					+ "							WHEN A.RVW_TYPE = 'item' AND A.RVW_REFER_NO = B.ITEM_NO\r\n"
					+ "							THEN B.ITEM_NAME\r\n"
					+ "							WHEN A.RVW_TYPE = 'class' AND A.RVW_REFER_NO = C.DAYCLASS_NO\r\n"
					+ "							THEN C.DAYCLASS_NAME\r\n"
					+ "				   		END AS RVW_REFER_NAME\r\n"
					+ "						FROM T_MB_REVIEW A\r\n"
					+ "				   			, T_MB_ITEM B\r\n"
					+ "				   			, T_MB_DAYCLASS C\r\n"
					+ "						WHERE A.RVW_REFER_NO = B.ITEM_NO\r\n"
					+ "				  			AND A.RVW_REFER_NO = C.DAYCLASS_NO\r\n"
					+ "				  			AND A.RVW_WRITER = :userId\r\n"
					+ "          ) F\r\n"
					+ "			LEFT OUTER JOIN T_MB_FILE D\r\n"
					+ "				ON F.RVW_TYPE = D.FILE_TYPE\r\n"
					+ "	  		 	AND F.RVW_REFER_NO = D.FILE_REFER_NO\r\n"
					+ "       		AND D.FILE_NO = (\r\n"
					+ "									SELECT MIN(E.FILE_NO)\r\n"
					+ "										FROM T_MB_FILE E\r\n"
					+ "                                		WHERE D.FILE_REFER_NO = E.FILE_REFER_NO\r\n"
					+ "                                  		AND D.FILE_TYPE = E.FILE_TYPE\r\n"
					+ "								)\r\n"
					+ " 		) T"
			, nativeQuery=true)
	Page<CamelHashMap> myPageRvwList(@Param("userId") String userId, Pageable pageable);
}
