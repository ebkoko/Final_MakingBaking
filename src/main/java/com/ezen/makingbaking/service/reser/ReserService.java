package com.ezen.makingbaking.service.reser;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Reser;

public interface ReserService {
	long getNextReserNo();
		
	void insertReser(Reser reser);
	
	Page<CamelHashMap> getReserList(String userId, Pageable pageable, String reserCondition);
	
	CamelHashMap getReserDetail(long reserNo);
	
	Reser updateReser(Reser reser);
}
