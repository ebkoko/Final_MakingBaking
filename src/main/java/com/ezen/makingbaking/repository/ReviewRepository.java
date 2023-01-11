package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.Review;

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
			+ " WHERE RVW_WRITER LIKE '%rvwWriter%'", nativeQuery=true)
	Review getUserRvwList(@Param("rvwWriter") String rvwWriter);
	
}
