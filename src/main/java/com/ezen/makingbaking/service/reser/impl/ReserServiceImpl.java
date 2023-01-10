package com.ezen.makingbaking.service.reser.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
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
	
	@Override
	public Page<CamelHashMap> getReserList(String userId, Pageable pageable, String reserCondition) {
		if(reserCondition == null || reserCondition.equals("") || reserCondition.equals("ALL")) {
			return reserRepository.findAllReser(userId, pageable);			
		} else {
			return reserRepository.findAllReserByReserCondition(userId, reserCondition, pageable);
		}
		
	}
	
	@Override
	public CamelHashMap getReserDetail(long reserNo) {
		return reserRepository.findByReserDetailAndReserNo(reserNo);
	}
	
	@Override
	public Reser updateReser(Reser reser) {
		return reserRepository.save(reser);
//		reserRepository.flush();
	}
}
