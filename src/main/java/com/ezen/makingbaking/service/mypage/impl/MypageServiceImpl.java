package com.ezen.makingbaking.service.mypage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.repository.BoardRepository;
import com.ezen.makingbaking.service.mypage.MypageService;

@Service
public class MypageServiceImpl implements MypageService{
	@Autowired
	private BoardRepository boardRepository;

	@Override
	public Page<CamelHashMap> getPageMyLikeList(String userId, Pageable pageable) {
		return boardRepository.getMyLikeList(userId, pageable);
	}

	@Override
	public void classUnlike(int likeNo, String userId) {
		boardRepository.classUnlike(likeNo, userId);
	}

	@Override
	public void itemUnlike(int likeNo, String userId) {
		boardRepository.itemUnlike(likeNo, userId);
	}


}
