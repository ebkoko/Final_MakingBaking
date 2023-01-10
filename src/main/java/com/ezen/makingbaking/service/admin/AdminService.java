package com.ezen.makingbaking.service.admin;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.User;

public interface AdminService {

	//item
	List<Item> getItemList(Item item);
	Page<Item> getPageItemList(Item item, Pageable pageable);
	
	void insertItem(Item item, List<ImgFile> uploadFileList);
	
	Item getItem(int itemNo);
	List<ImgFile> getItemFileList(int itemNo);
	
	Item updateItem(Item item, List<ImgFile> uFileList);
	
	void deleteItem(int itemNo);
	
	void saveItemList(List<Map<String, Object>> changeRowsList);
	
	//dayclass
	List<Dayclass> getDayclassList(Dayclass dayclass);
	Page<Dayclass> getPageDayclassList(Dayclass dayclass, Pageable pageable);
	
	void insertDayclass(Dayclass dayclass, List<ImgFile> uploadFileList);
	
	Dayclass getDayclass(int dayclassNo);
	List<ImgFile> getDayclassFileList(int dayclassNo);
	
	Dayclass updateDayclass(Dayclass dayclass, List<ImgFile> uFileList);
	
	void deleteDayclass(int dayclassNo);
	
	//user
	List<User> getUserList(User user);
	Page<User> getPageUserList(User user, Pageable pageable);
	
	//reser_dayclass
	List<Reser> getReserList(Reser reser);
	Page<Reser> getPageReserList(Reser reser, Pageable pageable);
	
}