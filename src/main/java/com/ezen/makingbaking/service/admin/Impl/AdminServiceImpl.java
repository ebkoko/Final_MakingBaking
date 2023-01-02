package com.ezen.makingbaking.service.admin.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.repository.ImgFileRepository;
import com.ezen.makingbaking.repository.ItemRepository;
import com.ezen.makingbaking.service.admin.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private ImgFileRepository imgFileRepository;
	

	//item
	@Override
	public List<Item> getItemList(Item item) {
		return itemRepository.findAll();
	}

	@Override
	public Page<Item> getPageItemList(Item item, Pageable pageable) {
		if(item.getSearchKeyword() != null && !item.getSearchKeyword().equals("")) {
			if(item.getSearchCondition().equals("ALL")) {
				return itemRepository.findByItemNameContainingOrItemCateContainingOrItemStatus
			               (item.getSearchKeyword(), 
			            	item.getSearchKeyword(),
			            	item.getSearchKeyword().charAt(0),
			            	pageable);
			      } else if (item.getSearchCondition().equals("ITEMNAME")) {
			         return itemRepository.findByItemNameContaining(item.getSearchKeyword(), pageable);
			      } else if (item.getSearchCondition().equals("ITEMCATE")) {
			    	  return itemRepository.findByItemCateContaining(item.getSearchKeyword(), pageable);
			      }  else if (item.getSearchCondition().equals("ITEMSTATUS")) {
				         return itemRepository.findByItemStatus(item.getSearchKeyword().charAt(0), pageable);
				  } else {
			    	  return itemRepository.findAll(pageable);
			      }
		  } else {
			  return itemRepository.findAll(pageable);
		  }
	      
	   }

	
	@Override
	public void insertItem(Item item, List<ImgFile> uploadFileList) {
		itemRepository.save(item);
		itemRepository.flush();
		
		for(ImgFile imgFile : uploadFileList) {
			imgFile.setFileReferNo(item.getItemNo());
			
			int imgFileNo = imgFileRepository.getMaxFileNo(item.getItemNo());
			imgFile.setFileNo(imgFileNo);
			imgFile.setFileType("item");
			imgFileRepository.save(imgFile);
		}
		
	}
	
	@Override
	public Item getItem(int itemNo) {
		return itemRepository.findById(itemNo).get();
	}

	@Override
	public List<ImgFile> getItemFileList(int itemNo) {
		return imgFileRepository.findByFileReferNoAndFileType(itemNo, "item");
	}
	
	
	@Override
	public void deleteItem(int itemNo) {
		itemRepository.deleteById(itemNo);
		
	}

	

	
	

//	@Override
//	public Page<CamelHashMap> getItemFileList(@PageableDefault(page = 0, size = 10) Pageable pageable) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	Page<CamelHashMap> getItemFileList(Page pageable) {
//		return itemRepository.getItemFileList(pageable);
//	}
	
	
}
