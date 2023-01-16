package com.ezen.makingbaking.service.mypage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;

public interface MypageService {
	Page<CamelHashMap> getPageMyLikeList(String userId, Pageable pageable);

	void classUnlike(int likeNo, String userId);

	void itemUnlike(int likeNo, String userId);
}
