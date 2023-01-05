package com.ezen.makingbaking.service.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.ItemLike;
import com.ezen.makingbaking.entity.ItemLikeId;
import com.ezen.makingbaking.repository.ItemLikeRepository;
import com.ezen.makingbaking.repository.ItemRepository;
import com.ezen.makingbaking.service.item.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Override
	public int getItemStock(int itemNo) {
		return itemRepository.findItemStockByItemNo(itemNo);
	}
	
	@Override
	public Page<CamelHashMap> getItemList(Pageable pageable) {
		return itemRepository.findItemAndFile(pageable);
	}
	
	@Override
	public Page<Item> getPageItemList(Pageable pageable) {
		return itemRepository.findAll(pageable);
	}
	
	
	@Autowired
	private ItemLikeRepository itemLikeRepository;
	
	@Override
	public Item getItem(int itemNo) {
		return itemRepository.findById(itemNo).get();
	}

	@Override
	public String getLikeYn(String loginUserId, int itemNo) {
		ItemLikeId itemLikeId = new ItemLikeId();
		
		itemLikeId.setItemNo(itemNo);
		itemLikeId.setUserId(loginUserId);
		
		if(itemLikeRepository.findById(itemLikeId).isPresent()) {
			return "Y";
		} else {
			return "N";
		}
		
	}

	@Override
	public int getLikeCnt(int itemNo) {
		// TODO Auto-generated method stub
		return itemLikeRepository.countByItemNo(itemNo);
	}

	@Override
	public void insertLike(int itemNo, String userId) {
		// TODO Auto-generated method stub
		ItemLike itemLike = new ItemLike();
		
		itemLike.setItemNo(itemNo);
		itemLike.setUserId(userId);
		
		itemLikeRepository.save(itemLike);
	}

	@Override
	public void deleteLike(int itemNo, String userId) {
		// TODO Auto-generated method stub
		ItemLikeId itemLikeId = new ItemLikeId();
		
		itemLikeId.setItemNo(itemNo);
		itemLikeId.setUserId(userId);
		
		itemLikeRepository.deleteById(itemLikeId);
	}

}