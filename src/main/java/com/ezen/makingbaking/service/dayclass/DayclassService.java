package com.ezen.makingbaking.service.dayclass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Dayclass;

public interface DayclassService {
	Dayclass getDayclass (int dayclassNo);
	
	Page<Dayclass> getOneDayclass(Pageable pageable);
	
	String getLikeYn(String loginUserId, int dayclassNo);
	
	int getLikeCnt(int dayclassNo);
	
	void insertLike(int dayclassNo, String userId);
	
	void deleteLike(int dayclassNo, String userId);
	
	CamelHashMap getClassImg(int dayclassNo);
}
