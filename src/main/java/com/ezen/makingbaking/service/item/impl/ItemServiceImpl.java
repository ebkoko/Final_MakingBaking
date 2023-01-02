package com.ezen.makingbaking.service.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.Item;
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
}