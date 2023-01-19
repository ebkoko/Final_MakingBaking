package com.ezen.makingbaking.service.admin;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Board;
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
	//user_회원상세보기
	User getUserInfoCheck(String userId);
	//user_리뷰팝업	
	Page<Review> getUserRvwPageList(Review review, Pageable pageable);
	//user_QnA팝업	
	Page<Board> getUserQnAPageList(Board board, Pageable pageable);

	
	
	
	//주문 및 예약관리
	//reser_dayclass
	Page<Reser> getPageReserList(Reser reser, Pageable pageable);
	void updatePartiStatus(Reser reser);/////////////////////////////참여현황 업데이트
	//order_item
	Page<Order> getPageOrderList(Order order, Pageable pageable);
	void updateOrderStatus(Order order);/////////////////////////////주문상태 업데이트
	
	//리뷰관리
	Page<Review> getPageReviewList(Review review, Pageable pageable);
	
	void saveReviewList(List<Map<String, Object>> changeRowsList);
}