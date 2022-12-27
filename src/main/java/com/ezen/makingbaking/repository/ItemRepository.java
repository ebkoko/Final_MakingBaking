package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
//	@Query(value="", 
//			countQuery="" ,nativeQuery=true)
//	public Page<CamelHashMap> getItemFileList(@Param("fileNo") int fileNo, Page pageable);
	
	Page<Item> findByItemNameContaining(String searchKeyword, Pageable pageable); //이름
	Page<Item> findByItemCateContaining(String searchKeyword, Pageable pageable); //카테
	Page<Item> findByItemPriceContaining(String searchKeyword, Pageable pageable); //가격
	Page<Item> findByItemRegdateContaining(String searchKeyword, Pageable pageable); //등록일
	Page<Item> findByItemStatusContaining(String searchKeyword, Pageable pageable); //상태
	Page<Item> findByItemNameContainingOrItemCateContainingOrItemPriceContainingOrItemRegdateContainingOrItemStatusContaining(String searchKeyword1, String searchKeyword2, String searchKeyword3, String searchKeyword4, String searchKeyword5, Pageable pageable);

	
}
