package com.ezen.makingbaking.service.review;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Review;

public interface ReviewService {

	Page<Review> getReviewList(int dayclassNo, Pageable pageable, String searchCondition);

	Page<Review> itemReviewList(int itemNo, Pageable pageable, String searchCondition);

	void insertClassRvw(Review review);

	void insertItemRvw(Review review);

	Page<CamelHashMap> getPageMyRvwList(String userId, Pageable pageable);

	void deleteRvw(List<Map<String, Object>> changeRowsList);

	

	
	
}
