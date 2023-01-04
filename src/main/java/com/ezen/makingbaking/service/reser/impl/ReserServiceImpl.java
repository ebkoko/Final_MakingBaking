package com.ezen.makingbaking.service.reser.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.repository.ReserRepository;
import com.ezen.makingbaking.service.reser.ReserService;

@Service
public class ReserServiceImpl implements ReserService {
	@Autowired
	private ReserRepository reserRepository;
	
	@Override
	public long getNextReserNo() {
		return reserRepository.getNextReserNo();
	}
	
	@Override
	public void insertReser(Reser reser) {
		reserRepository.save(reser);
	}
}
