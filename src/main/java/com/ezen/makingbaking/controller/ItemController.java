package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.service.item.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/list")
	//ItemDTO 받기 item cate가 null이 
	public ModelAndView itemListView(@PageableDefault(page = 0, size = 8) Pageable pageable) {
		Page<CamelHashMap> itemList = itemService.getItemList(pageable);
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("list/list.html");
		mv.addObject("itemList", itemList);
		return mv;
	}
	
	@GetMapping("/item")
	public ModelAndView dayclassVeiw() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");
		return mv;
	}
	
	@GetMapping("/stock")
	public int getItemStock(@RequestParam("itemNo") int itemNo) {
		return itemService.getItemStock(itemNo);
	}
	
	@PostMapping("/list")
	public ResponseEntity<?> getPageItemList(@PageableDefault(page=0, size=4) Pageable pageable){
		ResponseDTO<ItemDTO> response = new ResponseDTO<>();
		System.out.println(pageable.getPageNumber());
		try {
			Page<Item> pageItemList = itemService.getPageItemList(pageable);
			
			Page<ItemDTO> pageItemDTOList = pageItemList.map(pageItem -> 
															 ItemDTO.builder()
															 		.itemNo(pageItem.getItemNo())
															 		.itemName(pageItem.getItemName())
															 		.itemPrice(pageItem.getItemPrice())
															 		.build()
															);
			response.setPageItems(pageItemDTOList);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/item/{itemNo}")
	public ModelAndView getItem(@PathVariable int itemNo, @PageableDefault(page = 0, size = 4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		
		
		Item item = itemService.getItem(itemNo);
		
		String loginUserId = "";
		
		if(customUser != null)
			loginUserId = customUser.getUser().getUserId();
		
		ItemDTO itemDTO = ItemDTO.builder()
								 .itemNo(item.getItemNo())
								 .itemName(item.getItemName())
								 .itemMinName(item.getItemMinName())
								 .itemPrice(item.getItemPrice())
								 .itemDetails(item.getItemDetails())
								 .itemStock(item.getItemStock())
								 .itemStatus(item.getItemStatus())
								 .itemRegdate(item.getItemRegdate().toString())
								 .itemExpDate(item.getItemExpDate().toString())
								 .itemOrigin(item.getItemOrigin())
								 .itemAllergyInfo(item.getItemAllergyInfo())
								 .build();
		
		Page<Review> pageReviewList = itemService.getReviewList(itemNo, pageable);
		
		Page<ReviewDTO> pageReviewDTOList = pageReviewList.map(review -> ReviewDTO.builder()
																		  .rvwNo(review.getRvwNo())
																		  .rvwReferNo(review.getRvwReferNo())
																		  .rvwType(review.getRvwType())
																		  .rvwContent(review.getRvwContent())
																		  .rvwWriter(review.getRvwWriter())
																		  
																		  .rvwRegdate(review.getRvwRegdate().toString())
																		  .rvwScore(review.getRvwScore())
																		  .build()
		);
		
	
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");
		
		mv.addObject("item", itemDTO);
		mv.addObject("pageReviewList", pageReviewDTOList);
	
		
	
		return mv;
	}
	
	
}