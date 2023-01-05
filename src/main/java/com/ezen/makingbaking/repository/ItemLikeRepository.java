package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.ItemLike;
import com.ezen.makingbaking.entity.ItemLikeId;

public interface ItemLikeRepository extends JpaRepository<ItemLike, ItemLikeId> {
	int countByItemNo(int itemNo);
}
