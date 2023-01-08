package com.ezen.makingbaking.service.admin.Impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.repository.DayclassRepository;
import com.ezen.makingbaking.repository.ImgFileRepository;
import com.ezen.makingbaking.repository.ItemRepository;
import com.ezen.makingbaking.service.admin.AdminService;


@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private DayclassRepository dayclassRepository;
	
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
			
			int imgFileNo = imgFileRepository.getItemMaxFileNo(item.getItemNo());
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
	public Item updateItem(Item item, List<ImgFile> uFileList) {
		itemRepository.save(item);
		
		itemRepository.flush();
	      
	      if(uFileList.size() > 0) {
	    	  for(int i = 0; i < uFileList.size() ; i++) {
	    		  System.out.println("uFileList==================================================" + uFileList.get(i).toString());
	    		  if(uFileList.get(i).getFileStatus().equals("U")) {
	    			  imgFileRepository.save(uFileList.get(i));
	    		  } else if(uFileList.get(i).getFileStatus().equals("D")) {
	    			  imgFileRepository.delete(uFileList.get(i));
	    		  } else if (uFileList.get(i).getFileStatus().equals("I")) {
	    			  //추가한 파일들은 boardNo은 가지고 있지만 boardFileNo가 없는 상태라
	    			  //boardFileNo를 추가
	    			  int itemFileNo = imgFileRepository.getItemMaxFileNo(item.getItemNo());
	    			  
	    			  uFileList.get(i).setFileNo(itemFileNo);
	    			  
	    			  imgFileRepository.save(uFileList.get(i));
	    		  }
	    	  }
	      }
	      
	      
	      System.out.println(item.toString());
	      return item;
	   }
	
	
	@Override
	public void deleteItem(int itemNo) {
		itemRepository.deleteById(itemNo);
		
	}

	@Override
	public void saveItemList(List<Map<String, Object>> changeRowsList) {
		// TODO Auto-generated method stub
		for(int i = 0; i < changeRowsList.size(); i++) {
			Item ditem = Item.builder()
							.itemNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("itemNo"))))
								.build();
			
			itemRepository.delete(ditem);
			 
		}
		
	}
		

	//dayclass
	@Override
	public List<Dayclass> getDayclassList(Dayclass dayclass) {
		return dayclassRepository.findAll();
	}

	@Override
	public Page<Dayclass> getPageDayclassList(Dayclass dayclass, Pageable pageable) {
		if(dayclass.getSearchKeyword() != null && !dayclass.getSearchKeyword().equals("")) {
			if(dayclass.getSearchCondition().equals("ALL")) {
				return dayclassRepository.findByDayclassNameContainingOrDayclassTimeContainingOrDayclassUseYn
			               (dayclass.getSearchKeyword(), 
			            	dayclass.getSearchKeyword().charAt(0),
			            	dayclass.getSearchKeyword().charAt(0),
			            	pageable);
			      } else if (dayclass.getSearchCondition().equals("DAYCLASSNAME")) {
			         return dayclassRepository.findByDayclassNameContaining(dayclass.getSearchKeyword(), pageable);
			      } else if (dayclass.getSearchCondition().equals("DAYCLASSTIEM")) {
			    	  return dayclassRepository.findByDayclassTimeContaining(dayclass.getSearchKeyword().charAt(0), pageable);
			      }  else if (dayclass.getSearchCondition().equals("DAYCLASSUSEYN")) {
				         return dayclassRepository.findByDayclassUseYn(dayclass.getSearchKeyword().charAt(0), pageable);
				  } else {
			    	  return dayclassRepository.findAll(pageable);
			      }
		  } else {
			  return dayclassRepository.findAll(pageable);
		  }
	      
	   }
	
	@Override
	public void insertDayclass(Dayclass dayclass, List<ImgFile> uploadFileList) {
		dayclassRepository.save(dayclass);
		dayclassRepository.flush();
		
		for(ImgFile imgFile : uploadFileList) {
			imgFile.setFileReferNo(dayclass.getDayclassNo());
			
			int imgFileNo = imgFileRepository.getDayclassMaxFileNo(dayclass.getDayclassNo());
			imgFile.setFileNo(imgFileNo);
			imgFile.setFileType("dayclass");
			imgFileRepository.save(imgFile);
		}
		
	}
	
	@Override
	public Dayclass getDayclass(int dayclassNo) {
		return dayclassRepository.findById(dayclassNo).get();
	}

	@Override
	public List<ImgFile> getDayclassFileList(int dayclassNo) {
		return imgFileRepository.findByFileReferNoAndFileType(dayclassNo, "dayclass");
	}
	
	@Override
	public Dayclass updateDayclass(Dayclass dayclass, List<ImgFile> uFileList) {
		dayclassRepository.save(dayclass);
		
		dayclassRepository.flush();
	      
	      if(uFileList.size() > 0) {
	    	  for(int i = 0; i < uFileList.size() ; i++) {
	    		  if(uFileList.get(i).getFileStatus().equals("U")) {
	    			  imgFileRepository.save(uFileList.get(i));
	    		  } else if(uFileList.get(i).getFileStatus().equals("D")) {
	    			  imgFileRepository.delete(uFileList.get(i));
	    		  } else if (uFileList.get(i).getFileStatus().equals("I")) {
	    			  //추가한 파일들은 boardNo은 가지고 있지만 boardFileNo가 없는 상태라
	    			  //boardFileNo를 추가
	    			  int dayclassFileNo = imgFileRepository.getDayclassMaxFileNo(dayclass.getDayclassNo());
	    			  
	    			  uFileList.get(i).setFileNo(dayclassFileNo);
	    			  
	    			  imgFileRepository.save(uFileList.get(i));
	    		  }
	    	  }
	      }
	      
	      
	      System.out.println(dayclass.toString());
	      return dayclass;
	   }

	@Override
	public void deleteDayclass(int dayclassNo) {
		dayclassRepository.deleteById(dayclassNo);
		
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
