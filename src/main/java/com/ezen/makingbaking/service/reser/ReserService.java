package com.ezen.makingbaking.service.reser;

import java.util.List;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Reser;

public interface ReserService {
	long getNextReserNo();
		
	void insertReser(Reser reser);
	
	List<CamelHashMap> getReserList(String userId);
	
	CamelHashMap getReserDetail(long reserNo);
}
