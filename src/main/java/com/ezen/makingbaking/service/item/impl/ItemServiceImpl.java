package com.ezen.makingbaking.service.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
