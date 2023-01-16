package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.ImgFileDTO;
import com.ezen.makingbaking.dto.ItemDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.ImgFile;
import com.ezen.makingbaking.entity.Item;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.item.ItemService;
import com.ezen.makingbaking.service.review.ReviewService;

@RestController
@RequestMapping("/item")
public class ItemController {
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/list")
	public ModelAndView itemListView(@PageableDefault(page = 0, size = 8) Pageable pageable,
			HttpServletRequest request) {
		String itemCate = "";
		
		if(request.getParameter("itemCate") != null) {
			itemCate = request.getParameter("itemCate");
		}
		
		Page<CamelHashMap> itemList = itemService.getItemList(pageable, itemCate);
		int listCnt = itemService.getItemListCnt(itemCate);
		
		ModelAndView mv = new ModelAndView();
		
		mv.setViewName("list/list.html");
		mv.addObject("itemList", itemList);
		
		mv.addObject("itemCate", itemCate);
		mv.addObject("listCnt", listCnt);
		
		return mv;
	}
	
	@GetMapping("/item")
	public ModelAndView itemVeiw() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");
		return mv;
	}
	
	@GetMapping("/stock")
	public int getItemStock(@RequestParam("itemNo") int itemNo) {
		return itemService.getItemStock(itemNo);
	}
	
	@PostMapping("/list")
	public ResponseEntity<?> getPageItemList(@PageableDefault(page=0, size=4) Pageable pageable,
			HttpServletRequest request){
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		System.out.println(pageable.getPageNumber());
		try {
			String itmeCate = "";
			
			if(request.getParameter("itemCate") != null) {
				itmeCate = request.getParameter("itemCate");
			}
			Page<CamelHashMap> pageItemList = itemService.getPageItemList(pageable, itmeCate);
			
			response.setPageItems(pageItemList);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@GetMapping("/item/{itemNo}")
	public ModelAndView getItem(@PathVariable int itemNo, @PageableDefault(page = 0, size = 4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser, HttpServletRequest request) {
		
		String searchCondition = "";
		
		if(request.getParameter("searchCondition") != null) {
			searchCondition = request.getParameter("searchCondition");
		}
		
		List<ImgFile> itemImgList = itemService.getItemImg(itemNo);
		
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
		
		List<ImgFileDTO> itemImgDTOList = new ArrayList<ImgFileDTO>();
		
		for(int i = 0; i < itemImgList.size(); i++) {
			ImgFileDTO imgFileDTO = ImgFileDTO.builder()
											  .fileReferNo(itemImgList.get(i).getFileReferNo())
											  .fileNo(itemImgList.get(i).getFileNo())
											  .fileName(itemImgList.get(i).getFileName())
											  .fileOriginName(itemImgList.get(i).getFileOriginName())
											  .filePath(itemImgList.get(i).getFilePath())
											  .fileType(itemImgList.get(i).getFileType())
											  .build();
											  
			itemImgDTOList.add(imgFileDTO);
		}
		
		Page<Review> pageReviewList = reviewService.itemReviewList(itemNo, pageable, searchCondition);
		
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
		
		String likeYn = "N";
		
		if(!loginUserId.equals(""))
			likeYn = itemService.getLikeYn(loginUserId, itemNo);
		
		int likeCnt = itemService.getLikeCnt(itemNo);
		
		List<CamelHashMap> reviewChkList = itemService.getUserOrderStatus(loginUserId, itemNo);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("item/getItem.html");		
		
		mv.addObject("item", itemDTO);
		mv.addObject("itemImgList", itemImgDTOList);
		mv.addObject("pageReviewList", pageReviewDTOList);		
		mv.addObject("likeYn", likeYn);
		mv.addObject("likeCnt", likeCnt);
		mv.addObject("searchCondition", searchCondition);
		mv.addObject("reviewChkList", reviewChkList);
		
		return mv;
	}
	// 리뷰 조건 검색
	@PostMapping("/item/{itemNo}")
	public ResponseEntity<?> getDayclassPage(@PathVariable int itemNo, 
			@PageableDefault(page = 0, size = 4) Pageable pageable, 
			@AuthenticationPrincipal CustomUserDetails customUser,
			HttpServletRequest request){
		
		ResponseDTO<ReviewDTO> response = new ResponseDTO<>();
		
		
		try {
			String searchCondition = "";
			
			if(request.getParameter("searchCondition") != null) {
				searchCondition = request.getParameter("searchCondition");
			}
			
			Page<Review> pageReviewList = reviewService.itemReviewList(itemNo, pageable, searchCondition);
			
			Page<ReviewDTO> pageReviewDTOList = pageReviewList.map(pageReview 
													-> ReviewDTO.builder()
																  .rvwNo(pageReview.getRvwNo())
																  .rvwReferNo(pageReview.getRvwReferNo())
																  .rvwType(pageReview.getRvwType())
																  .rvwContent(pageReview.getRvwContent())
																  .rvwWriter(pageReview.getRvwWriter())
																  
																  .rvwRegdate(pageReview.getRvwRegdate().toString())
																  .rvwScore(pageReview.getRvwScore())
																  .build()
									);
																				  
			response.setPageItems(pageReviewDTOList);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 좋아요
	@PostMapping("like")
	public ResponseEntity<?> insertLike(@RequestParam("itemNo") int itemNo,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
		
		try {
			itemService.insertLike(itemNo, customUser.getUsername());			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("likeYn", "Y");
			returnMap.put("likeCnt", itemService.getLikeCnt(itemNo));
			
			response.setItem(returnMap);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("like")
	public ResponseEntity<?> deleteLike(@RequestParam("itemNo") int itemNo,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
		
		try {
			itemService.deleteLike(itemNo, customUser.getUsername());			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("likeYn", "Y");
			returnMap.put("likeCnt", itemService.getLikeCnt(itemNo));
			
			response.setItem(returnMap);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 리뷰 등록
	@PostMapping("/insertItemRvw")
	public void insertItemRvw(ReviewDTO reviewDTO, HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		
		Review review = Review.builder()
							  .rvwScore(reviewDTO.getRvwScore())
							  .rvwWriter(customUser.getUsername())
							  .rvwRegdate(LocalDateTime.now())							 
							  .rvwContent(reviewDTO.getRvwContent())
							  .rvwReferNo(reviewDTO.getRvwReferNo())
							  .rvwType("item")
							  .rvwOrderNo(reviewDTO.getRvwOrderNo())
							  .build();
		
		reviewService.insertItemRvw(review);
		
	
	}
	
	
}