package com.ezen.makingbaking.service.item;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;


public interface ItemService {
	int getItemStock(int itemNo);
	
	Page<CamelHashMap> getItemList(Pageable pageable);
	
	Page<CamelHashMap> getPageItemList(Pageable pageable);

	Item getItem(int itemNo);
	
	String getLikeYn(String loginUserId, int itemNo);
	
	int getLikeCnt(int itemNo);
	
	void insertLike(int itemNo, String userId);

	void deleteLike(int itemNo, String userId);

	List<ImgFile> getItemImg(int itemNo);

	List<CamelHashMap> getUserOrderStatus(String loginUserId, int itemNo);
	

	
	
	
}
