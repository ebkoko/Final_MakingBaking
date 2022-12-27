package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	//Page<Review> findByDayclassNo (int dayclassNo, Pageable pageable);
	
	Page<Review> findByRvwReferNoAndRvwType (@Param("rvwReferNo") int dayclassNo, @Param("rvwType") String rvwType, Pageable pageable);
}
