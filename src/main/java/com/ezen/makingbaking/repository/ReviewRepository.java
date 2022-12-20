package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

}
