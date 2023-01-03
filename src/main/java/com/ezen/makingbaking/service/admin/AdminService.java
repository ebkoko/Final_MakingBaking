package com.ezen.makingbaking.service.admin;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;

public interface AdminService {
	//Page<CamelHashMap> getItemFileList(Pageable pageable);
	//item
	List<Item> getItemList(Item item);
	Page<Item> getPageItemList(Item item, Pageable pageable);
	
	void insertItem(Item item, List<ImgFile> uploadFileList);
	
	Item getItem(int itemNo);
	List<ImgFile> getItemFileList(int itemNo);
	
	Item updateItem(Item item, List<ImgFile> uFileList);
	
	void getUpdateItem(int itemNo);
	
	void deleteItem(int ItemNo);
}