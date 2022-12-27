package com.ezen.makingbaking.service.dayclass.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.repository.DayclassRepository;
import com.ezen.makingbaking.service.dayclass.DayclassService;

@Service
public class DayclassServiceImpl implements DayclassService {
	@Autowired
	private DayclassRepository dayclassRepository;

	@Override
	public Dayclass getDayclass(int dayclassNo) {
		// TODO Auto-generated method stub
		return dayclassRepository.findById(dayclassNo).get();
	}
}
