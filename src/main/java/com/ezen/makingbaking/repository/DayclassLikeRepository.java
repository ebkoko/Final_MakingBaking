package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.DayclassLike;
import com.ezen.makingbaking.entity.DayclassLikeId;

public interface DayclassLikeRepository extends JpaRepository<DayclassLike, DayclassLikeId>{
	int countByDayclassNo(int dayclassNo);
}	
