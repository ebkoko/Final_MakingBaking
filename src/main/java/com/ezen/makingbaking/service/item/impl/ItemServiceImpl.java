package com.ezen.makingbaking.service.item.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.ItemLike;
import com.ezen.makingbaking.entity.ItemLikeId;
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.repository.ImgFileRepository;
import com.ezen.makingbaking.repository.ItemLikeRepository;
import com.ezen.makingbaking.repository.ItemRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.service.item.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ImgFileRepository imgFileRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public int getItemStock(int itemNo) {
		return itemRepository.findItemStockByItemNo(itemNo);
	}
	
	
	@Override
	public Page<CamelHashMap> getItemList(Pageable pageable, String itemCate) {
		if(itemCate.equals("") || itemCate.equals("ALL"))
			return itemRepository.findItemAndFile(pageable);
		else
			return itemRepository.findItemAndFile(pageable, itemCate);
	}
	
	@Override
	public Page<CamelHashMap> getPageItemList(Pageable pageable, String itemCate) {
		if(itemCate.equals("") || itemCate.equals("ALL"))
			return itemRepository.findItemAndFile(pageable);
		else
			return itemRepository.findItemAndFile(pageable, itemCate);
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
	
	@Override
	public List<ImgFile> getItemImg(int itemNo) {
		return imgFileRepository.findByFileReferNoAndFileType(itemNo, "item");
	}

	@Override
	public List<CamelHashMap> getUserOrderStatus(String loginUserId, int itemNo) {	
		return orderRepository.getByUserIdAndItemNo(loginUserId, itemNo);
	}


	@Override
	public int getItemListCnt(String itemCate) {
		// TODO Auto-generated method stub
		if(itemCate.equals("") || itemCate.equals("ALL"))
			return itemRepository.getItemListCnt();
		else
			return itemRepository.getItemListCnt(itemCate);
	}
}