package com.ezen.makingbaking.service.reser;

import com.ezen.makingbaking.entity.Reser;

public interface ReserService {
	long getNextReserNo();
	
	void insertReser(Reser reser);
}
