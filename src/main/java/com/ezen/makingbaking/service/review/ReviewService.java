package com.ezen.makingbaking.service.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Review;

public interface ReviewService {

	Page<Review> getReviewList(int dayclassNo, Pageable pageable, String searchCondition);

	Page<Review> itemReviewList(int itemNo, Pageable pageable, String searchCondition);

	
}
