package com.ezen.makingbaking.service.admin;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Item;

public interface AdminService {
	//Page<CamelHashMap> getItemFileList(Pageable pageable);
	
	List<Item> getItemList(Item item);
	
	Page<Item> getPageItemList(Item item, Pageable pageable);
}