package com.ezen.makingbaking.service.dayclass;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.Reser;

public interface DayclassService {
	Dayclass getDayclass (int dayclassNo);
	
	Page<CamelHashMap> getOneDayclass(Pageable pageable);
	
	String getLikeYn(String loginUserId, int dayclassNo);
	
	int getLikeCnt(int dayclassNo);
	
	void insertLike(int dayclassNo, String userId);
	
	void deleteLike(int dayclassNo, String userId);
	
	CamelHashMap getClassImg(int dayclassNo);
	
	int getPersonCnt(Reser reser);

	List<CamelHashMap> getUserReserStatus(String loginUserId, int dayclassNo);
	
}
