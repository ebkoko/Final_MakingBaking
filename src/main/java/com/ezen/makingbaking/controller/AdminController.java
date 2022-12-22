package com.ezen.makingbaking.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.service.admin.AdminService;
import com.ezen.makingbaking.service.item.ItemService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	private AdminService adminService;
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/adminView")
	public ModelAndView adminListView() {
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("admin/itemList.html");
		return mv;
	}
	
//	@GetMapping("/itemList")
//	public ModelAndView getItemList(ItemDTO itemDTO,
//			@PageableDefault(page = 0, size = 10) Pageable pageable) {
//		Item item = Item.builder()
//						.itemName(itemDTO.getItemName())
//						.itemPrice(itemDTO.getItemPrice())
//						.itemStatus(itemDTO.getItemStatus())
//						.searchCondition(itemDTO.getSearchCondition())
//						.searchKeyword(itemDTO.getSearchKeyword())
//						.build();
//		List<Item> itemList = itemService.getItemList(item);
//		
//		Page<Item> pageItemList = itemService.getPageItemList(item, pageable);
//		
//		Page<ItemDTO>pageItemDTOList = pageItemList.map(pageItem ->
//		ItemDTO.builder()
//				.itemNo(pageItem.getItemNo())
//				.itemName(pageItem.getItemName())
//				.itemPrice(pageItem.getItemPrice())
//				.itemRegdate(
//							pageItem.getItemRegdate() == null?
//							null :
//							pageItem.getItemRegdate().toString())
//				.itemStatus(getItem.getItemStatus())
//				.build()
//			);
//
//		List<ItemDTO> getItemList = new ArrayList<ItemDTO>();
//		for(int i = 0; i < boardList.size(); i++) {
//			ItemDTO returnItem = ItemDTO.builder()
//										.itemNo(itemList.get(i).getItemNo())
//										.itemName(itemList.get(i).getItemName())
//										.itemPrice(itemList.get(i).getItemPrice())
//										.itemRegdate(
//												itemList.get(i).getItemRegdate() == null?
//												null :
//												itemList.get(i).getItemRegdate().toString())
//										.itemStatus(itemList.get(i).getItemStatus())
//										.build();
//			
//			getItemList.add(returnItem);
//		}
//		
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("item.itemList.html");
//		mv.addObject("getItemList", pageItemList);
//		
//		if(itemDTO.getSearchCondition() != null && !itemDTO.getSearchCondition().equals("")) {
//			mv.addObject("searchCondition", itemDTO.getSearchCondition());
//		}
//		
//		if(itemDTO.getSearchKeyword() != null && !itemDTO.getSearchKeyword().equals("")) {
//			mv.addObject("searchKeyword", itemDTO.getSearchKeyword());
//		}
//		return mv;)
//	}
	
	//@GetMapping("/")
	//public ModelAndView getItemFileList(Page pageable) { 
		//admin
	//}
}
