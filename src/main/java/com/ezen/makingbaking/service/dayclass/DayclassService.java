package com.ezen.makingbaking.service.dayclass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Dayclass;

public interface DayclassService {
	Dayclass getDayclass (int dayclassNo);
	
	Page<Dayclass> getOneDayclass(Pageable pageable);
}
