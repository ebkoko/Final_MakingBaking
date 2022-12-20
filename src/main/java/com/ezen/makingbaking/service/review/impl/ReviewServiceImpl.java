package com.ezen.makingbaking.service.review.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.repository.ReviewRepository;
import com.ezen.makingbaking.service.review.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
	@Autowired
	private ReviewRepository reviewRepository;
}
