package com.ezen.makingbaking.service.admin;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.entity.User;

public interface AdminService {

	//item_리스트, 등록, 수정, 삭제
	Page<Item> getPageItemList(Item item, Pageable pageable);
	
	void insertItem(Item item, List<ImgFile> uploadFileList);
	
	Item getItem(int itemNo);
	List<ImgFile> getItemFileList(int itemNo);
	
	Item updateItem(Item item, List<ImgFile> uFileList);
	
	void deleteItem(int itemNo);
	
	void saveItemList(List<Map<String, Object>> changeRowsList);
	
	//dayclass_리스트, 등록, 수정, 삭제
	Page<Dayclass> getPageDayclassList(Dayclass dayclass, Pageable pageable);
	
	void insertDayclass(Dayclass dayclass, List<ImgFile> uploadFileList);
	
	Dayclass getDayclass(int dayclassNo);
	List<ImgFile> getDayclassFileList(int dayclassNo);
	
	Dayclass updateDayclass(Dayclass dayclass, List<ImgFile> uFileList);
	
	void deleteDayclass(int dayclassNo);
	
	void saveDayclassList(List<Map<String, Object>> changeRowsList);
	
	//user
	Page<User> getPageUserList(User user, Pageable pageable);
	
	void saveUserList(List<Map<String, Object>> changeRowsList);
	
	//user_review
	Page<Review> getUserRvwList(String rvwWriter, Pageable pageable);
	
	
	//reser_dayclass
	Page<Reser> getPageReserList(Reser reser, Pageable pageable);
	
	//order_item
	Page<Order> getPageOrderList(Order order, Pageable pageable);
	
}