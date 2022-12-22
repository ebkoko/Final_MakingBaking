package com.ezen.makingbaking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
//	@Query(value="", 
//			countQuery="" ,nativeQuery=true)
//	public Page<CamelHashMap> getItemFileList(@Param("fileNo") int fileNo, Page pageable);
}
