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
import com.ezen.makingbaking.entity.Order;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.repository.DayclassRepository;
import com.ezen.makingbaking.repository.ImgFileRepository;
import com.ezen.makingbaking.repository.ItemRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.repository.ReserRepository;
import com.ezen.makingbaking.repository.ReviewRepository;
import com.ezen.makingbaking.repository.UserRepository;
import com.ezen.makingbaking.service.admin.AdminService;


@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	private ItemRepository itemRepository;
	
	@Autowired
	private DayclassRepository dayclassRepository;
	
	@Autowired
	private ImgFileRepository imgFileRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReserRepository reserRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	

	//item
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
		for(int i = 0; i < changeRowsList.size(); i++) {
			Item ditem = Item.builder()
							.itemNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("itemNo"))))
								.build();
			
			ImgFile dImgFile = ImgFile.builder()
									  .fileReferNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("itemNo"))))
									  .fileType("item")
									  .build();
			
			itemRepository.delete(ditem);
			imgFileRepository.delete(dImgFile);
			 
		}
		
	}
		

	//dayclass
	@Override
	public Page<Dayclass> getPageDayclassList(Dayclass dayclass, Pageable pageable) {
		if(dayclass.getSearchKeyword() != null && !dayclass.getSearchKeyword().equals("")) {
			if(dayclass.getSearchCondition().equals("ALL")) {
				return dayclassRepository.findByDayclassNameContainingOrDayclassTimeOrDayclassUseYn
			               (dayclass.getSearchKeyword(), 
			            	dayclass.getSearchKeyword().charAt(0),
			            	dayclass.getSearchKeyword().charAt(0),
			            	pageable);
			      } else if (dayclass.getSearchCondition().equals("DAYCLASSNAME")) {
			         return dayclassRepository.findByDayclassNameContaining(dayclass.getSearchKeyword(), pageable);
			      } else if (dayclass.getSearchCondition().equals("DAYCLASSTIME")) {
			    	  return dayclassRepository.findByDayclassTime(dayclass.getSearchKeyword().charAt(0), pageable);
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
	
	@Override
	public void saveDayclassList(List<Map<String, Object>> changeRowsList) {
		for(int i = 0; i < changeRowsList.size(); i++) {
			Dayclass ddayclass = Dayclass.builder()
										.dayclassNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("dayclassNo"))))
										.build();
			
			ImgFile dImgFile = ImgFile.builder()
									  .fileReferNo(Integer.parseInt(String.valueOf(changeRowsList.get(i).get("dayclassNo"))))
									  .fileType("dayclass")
									  .build();
			
			dayclassRepository.delete(ddayclass);
			imgFileRepository.delete(dImgFile);
			 
		}
		
	}
	
	
	//user
	@Override
	public Page<User> getPageUserList(User user, Pageable pageable) {
		if(user.getSearchKeyword() != null && !user.getSearchKeyword().equals("")) {
			if(user.getSearchCondition().equals("ALL")) {
				return userRepository.findByUserNameContainingOrUserId
						(user.getSearchKeyword(), user.getSearchKeyword(), pageable);
			      } else if (user.getSearchCondition().equals("USERNAME")) {
			         return userRepository.findByUserNameContaining(user.getSearchKeyword(), pageable);
			      } else if (user.getSearchCondition().equals("USERID")) {
			    	  return userRepository.findByUserIdContaining(user.getSearchKeyword(), pageable);
			      } else {
			    	  return userRepository.findAll(pageable);
			      }
		  } else {
			  return userRepository.findAll(pageable);
		  }
	      
	   }
	
	@Override
	public Review getUserRvwList(String rvwWriter) {
		return reviewRepository.getUserRvwList(rvwWriter);
	}
	
	
	//reser_dayclassList
	@Override
	public Page<Reser> getPageReserList(Reser reser, Pageable pageable) {
		if(reser.getSearchKeyword() != null && !reser.getSearchKeyword().equals("")) {
			if(reser.getSearchCondition().equals("ALL")) {
				return reserRepository.findByReserNoOrPartiNameContainingOrClassNoOrPartiDateContainingOrPartiTimeContainingOrReserStatusContainingOrPartiStatus
						(Long.parseLong(reser.getSearchKeyword()), reser.getSearchKeyword(), Integer.parseInt(reser.getSearchKeyword()), 
								reser.getSearchKeyword(), reser.getSearchKeyword(), reser.getSearchKeyword(), reser.getSearchKeyword(), pageable);
			      } else if (reser.getSearchCondition().equals("RESERNO")) {
			         return reserRepository.findByReserNo(Long.parseLong(reser.getSearchKeyword()), pageable);
			      } else if (reser.getSearchCondition().equals("PARTINAME")) {
			    	  return reserRepository.findByPartiNameContaining(reser.getSearchKeyword(), pageable);
			      } else if (reser.getSearchCondition().equals("CLASSNO")) {
			    	  return reserRepository.findByClassNo(Integer.parseInt(reser.getSearchKeyword()), pageable);
			      } else if (reser.getSearchCondition().equals("PARTIDATE")) {
				         return reserRepository.findByPartiDateContaining(reser.getSearchKeyword(), pageable);
			      } else if (reser.getSearchCondition().equals("PARTITIME")) {
			    	  return reserRepository.findByPartiTimeContaining(reser.getSearchKeyword(), pageable);
			      } else if (reser.getSearchCondition().equals("RESERSTATUS")) {
			    	  return reserRepository.findByReserStatusContaining(reser.getSearchKeyword(), pageable);
			      } else if (reser.getSearchCondition().equals("PARTISTATUS")) {
			    	  return reserRepository.findByPartiStatusContaining(reser.getSearchKeyword(), pageable);
			      } else {
			    	  return reserRepository.findAll(pageable);
			      }
		  } else {
			  return reserRepository.findAll(pageable);
		  }
	      
	   }
	
	//order_itemList
	@Override
	public Page<Order> getPageOrderList(Order order, Pageable pageable) {
		if(order.getSearchKeyword() != null && !order.getSearchKeyword().equals("")) {
			if(order.getSearchCondition().equals("ALL")) {
				return orderRepository.findByOrderNoOrUserIdContainingOrOrderNameContainingOrOrderPaymentContainingOrOrderStatus
						(Long.parseLong(order.getSearchKeyword()), order.getSearchKeyword(),
								order.getSearchKeyword(), order.getSearchKeyword(), order.getSearchKeyword(), pageable);
			      } else if (order.getSearchCondition().equals("ORDERNO")) {
			         return orderRepository.findByOrderNo(Long.parseLong(order.getSearchKeyword()), pageable);
			      } else if (order.getSearchCondition().equals("USERID")) {
			    	  return orderRepository.findByUserIdContaining(order.getSearchKeyword(), pageable);
			      } else if (order.getSearchCondition().equals("ORDERNAME")) {
				         return orderRepository.findByOrderNameContaining(order.getSearchKeyword(), pageable);
			      } else if (order.getSearchCondition().equals("ORDERPAYMENT")) {
			    	  return orderRepository.findByOrderPaymentContaining(order.getSearchKeyword(), pageable);
			      } else if (order.getSearchCondition().equals("ORDERSTATUS")) {
			    	  return orderRepository.findByOrderStatusContaining(order.getSearchKeyword(), pageable);
			      } else {
			    	  return orderRepository.findAll(pageable);
			      }
		  } else {
			  return orderRepository.findAll(pageable);
		  }
	      
	   }
	

	
	
}
