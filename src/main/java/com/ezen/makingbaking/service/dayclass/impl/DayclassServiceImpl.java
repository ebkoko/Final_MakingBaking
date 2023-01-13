package com.ezen.makingbaking.service.dayclass.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.DayclassLike;
import com.ezen.makingbaking.entity.DayclassLikeId;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.repository.DayclassLikeRepository;
import com.ezen.makingbaking.repository.DayclassRepository;
import com.ezen.makingbaking.repository.ReserRepository;
import com.ezen.makingbaking.service.dayclass.DayclassService;

@Service
public class DayclassServiceImpl implements DayclassService {
	@Autowired
	private DayclassRepository dayclassRepository;
	
	@Autowired
	private DayclassLikeRepository dayclassLikeRepository;
	
	@Autowired
	private ReserRepository reserRepository;

	@Override
	public Dayclass getDayclass(int dayclassNo) {
		// TODO Auto-generated method stub
		return dayclassRepository.findById(dayclassNo).get();
	}
	
	@Override
	public Page<CamelHashMap> getOneDayclass(Pageable pageable) {
		return dayclassRepository.findDayclassAndFile(pageable);
	}

	@Override
	public String getLikeYn(String loginUserId, int dayclassNo) {
		DayclassLikeId dayclassLikeId = new DayclassLikeId();
		
		dayclassLikeId.setDayclassNo(dayclassNo);
		dayclassLikeId.setUserId(loginUserId);
		
		if(dayclassLikeRepository.findById(dayclassLikeId).isPresent()) {
			return "Y";
		} else {
			return "N";
		}
	}

	@Override
	public int getLikeCnt(int dayclassNo) {
		return dayclassLikeRepository.countByDayclassNo(dayclassNo);
	}

	@Override
	public void insertLike(int dayclassNo, String userId) {
		// TODO Auto-generated method stub
		DayclassLike dayclassLike = new DayclassLike();
		
		dayclassLike.setDayclassNo(dayclassNo);
		dayclassLike.setUserId(userId);
		
		dayclassLikeRepository.save(dayclassLike);
	}

	@Override
	public void deleteLike(int dayclassNo, String userId) {
		// TODO Auto-generated method stub
		DayclassLikeId dayclassLikeId = new DayclassLikeId();
		
		dayclassLikeId.setDayclassNo(dayclassNo);
		dayclassLikeId.setUserId(userId);
		
		dayclassLikeRepository.deleteById(dayclassLikeId);
	}
	
	@Override
	public CamelHashMap getClassImg(int dayclassNo) {
		return dayclassRepository.findByFileNoAndDayclassNo(dayclassNo);
	}
	
	@Override
	public int getPersonCnt(Reser reser) {
		return reserRepository.getPersonCnt(reser);
	}

	@Override
	public List<CamelHashMap> getUserReserStatus(String loginUserId, int dayclassNo) {
		return reserRepository.getByUserIdAndClassNo(loginUserId, dayclassNo);
	}
}
