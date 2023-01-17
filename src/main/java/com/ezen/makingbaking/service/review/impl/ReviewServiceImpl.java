package com.ezen.makingbaking.service.review.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.repository.ReviewRepository;
import com.ezen.makingbaking.service.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public Page<Review> getReviewList(int dayclassNo, Pageable pageable, String searchCondition) {
		// TODO Auto-generated method stub
		if(searchCondition.equals("HIGH")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwScoreDesc(dayclassNo, "class", pageable);
		} else if(searchCondition.equals("LOW")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwScoreAsc(dayclassNo, "class", pageable);
		} else if(searchCondition.equals("OLD")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwRegdateAsc(dayclassNo, "class", pageable);
		} else {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwRegdateDesc(dayclassNo, "class", pageable);
		}
	}

	@Override
	public Page<Review> itemReviewList(int itemNo, Pageable pageable, String searchCondition) {
		if(searchCondition.equals("HIGH")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwScoreDesc(itemNo, "item", pageable);
		} else if(searchCondition.equals("LOW")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwScoreAsc(itemNo, "item", pageable);
		} else if(searchCondition.equals("OLD")) {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwRegdateAsc(itemNo, "item", pageable);
		} else {
			return reviewRepository.findByRvwReferNoAndRvwTypeOrderByRvwRegdateDesc(itemNo, "item" ,pageable);	
		}
	
	}

	@Override
	public void insertClassRvw(Review review) {
		reviewRepository.save(review);
		
	}

	@Override
	public void insertItemRvw(Review review) {
		reviewRepository.save(review);
		
	}

	@Override
	public Page<CamelHashMap> getPageMyRvwList(String userId, Pageable pageable) {
		return reviewRepository.myPageRvwList(userId, pageable);
	}

	@Override
	public void deleteRvw(List<Map<String, Object>> changeRowsList) {
		for(int i = 0; i < changeRowsList.size(); i++) {
			Review review = Review.builder()
							.rvwNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("rvwNo"))))
							.build();
			
			reviewRepository.delete(review);
			 
		}
		
	}



	

	
}