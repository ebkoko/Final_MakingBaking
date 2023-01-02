package com.ezen.makingbaking.service.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;

public interface ItemService {
	int getItemStock(int itemNo);
	
	Page<CamelHashMap> getItemList(Pageable pageable);
	
	Page<Item> getPageItemList(Pageable pageable);
}
