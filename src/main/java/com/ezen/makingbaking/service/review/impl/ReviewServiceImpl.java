package com.ezen.makingbaking.service.review.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.repository.ReviewRepository;
import com.ezen.makingbaking.service.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public Page<Review> getReviewList(int dayclassNo, Pageable pageable) {
		// TODO Auto-generated method stub
		return reviewRepository.findByRvwReferNoAndRvwType(dayclassNo, "class", pageable);
	}



}
