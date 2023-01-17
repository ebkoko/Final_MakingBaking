package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.Reser;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.dayclass.DayclassService;
import com.ezen.makingbaking.service.review.ReviewService;
import com.ezen.makingbaking.service.user.UserService;


@RestController
@RequestMapping("/dayclass")
public class DayclassController {
	@Autowired
	private DayclassService dayclassService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/onedayClass")
	public ModelAndView onedayClassView(@PageableDefault(page = 0, size = 20) Pageable pageable) {
		Page<CamelHashMap> dayclassList = dayclassService.getOneDayclass(pageable);

		//System.out.println(dayclassDTOList.getContent().get(0).getDayclassNo());
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/onedayClass.html");
		mv.addObject("dayclassList", dayclassList);
		return mv;
	}
	
	@GetMapping("/dayclass/{dayclassNo}")
	public ModelAndView getDayclass(@PathVariable int dayclassNo, @PageableDefault(page = 0, size = 4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser, HttpServletRequest request) {
		String searchCondition = "";
		
		if(request.getParameter("searchCondition") != null) {
			searchCondition = request.getParameter("searchCondition");
		}
		
		CamelHashMap dayclass = dayclassService.getClassImg(dayclassNo);
		
		String loginUserId = "";
		
		if(customUser != null)
			loginUserId = customUser.getUser().getUserId();
		

		
		Page<Review> reviewList = reviewService.getReviewList(dayclassNo, pageable, searchCondition);
		
		Page<ReviewDTO> reviewDTOList = reviewList.map(review -> ReviewDTO.builder()
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
			likeYn = dayclassService.getLikeYn(loginUserId, dayclassNo);
		
		int likeCnt = dayclassService.getLikeCnt(dayclassNo);
		
		List<CamelHashMap> reviewChkList = dayclassService.getUserReserStatus(loginUserId, dayclassNo);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/getDayclass.html");
		
		mv.addObject("dayclass", dayclass);
		mv.addObject("reviewList", reviewDTOList);
		mv.addObject("likeYn", likeYn);
		mv.addObject("likeCnt", likeCnt);
		mv.addObject("searchCondition", searchCondition);
		mv.addObject("reviewChkList", reviewChkList);
		
		System.out.println("+++++++++++++++++++++++++++++++++++++" + reviewChkList.size());
		
		return mv;
	}
	
	// 리뷰 조건 검색
	@PostMapping("/dayclass/{dayclassNo}")
	public ResponseEntity<?> getDayclassPage(@PathVariable int dayclassNo, 
			@PageableDefault(page = 0, size = 4) Pageable pageable, 
			@AuthenticationPrincipal CustomUserDetails customUser,
			HttpServletRequest request){
		
		ResponseDTO<ReviewDTO> response = new ResponseDTO<>();
		
		
		try {
			String searchCondition = "";
			
			if(request.getParameter("searchCondition") != null) {
				searchCondition = request.getParameter("searchCondition");
			}
			
			Page<Review> pageReviewList = reviewService.getReviewList(dayclassNo, pageable, searchCondition);
			
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
	public ResponseEntity<?> insertLike(@RequestParam("dayclassNo") int dayclassNo,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
		
		try {
			dayclassService.insertLike(dayclassNo, customUser.getUsername());			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("likeYn", "Y");
			returnMap.put("likeCnt", dayclassService.getLikeCnt(dayclassNo));
			
			response.setItem(returnMap);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@DeleteMapping("like")
	public ResponseEntity<?> deleteLike(@RequestParam("dayclassNo") int dayclassNo,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<Map<String, Object>> response = new ResponseDTO<>();
		
		try {
			dayclassService.deleteLike(dayclassNo, customUser.getUsername());			
			
			Map<String, Object> returnMap = new HashMap<String, Object>();
			returnMap.put("likeYn", "Y");
			returnMap.put("likeCnt", dayclassService.getLikeCnt(dayclassNo));
			
			response.setItem(returnMap);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	

	//
	@PostMapping("/reserDayclass")
	public ModelAndView moveReserPage(@RequestParam("dayclassNo") int dayclassNo, 
			@RequestParam Map<String, String> paramMap, @AuthenticationPrincipal CustomUserDetails customUser) {
		User user = customUser.getUser();
		
		CamelHashMap dayclass = dayclassService.getClassImg(dayclassNo);
		
		ModelAndView mv = new ModelAndView();
		
		user = userService.idcheck(user);
		
		mv.addObject("userInfo", user);
		mv.addObject("dayclass", dayclass);
		mv.addObject("reserPersonCnt", paramMap.get("reserPersonCnt"));
		mv.addObject("reserDate", paramMap.get("reserDate"));
		mv.addObject("partiTime", paramMap.get("partiTime"));
		mv.setViewName("/reser/reser.html");
		return mv;
	}
	
	@PostMapping("/getPersonCnt")
	public ResponseEntity<?> getPersonCnt(ReserDTO reserDTO) {
		ResponseDTO<Integer> response = new ResponseDTO<>();
		
		try {
			Reser reser = Reser.builder()
								.classNo(reserDTO.getClassNo())
								.partiDate(reserDTO.getPartiDate())
								.partiTime(reserDTO.getPartiTime())
								.build();
			
			int personCnt = dayclassService.getPersonCnt(reser);
			
			response.setItem(personCnt);
			
			return ResponseEntity.ok().body(response);			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 리뷰 등록
	@PostMapping("/insertClassRvw")
	public void insertClassRvw(ReviewDTO reviewDTO, HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		
		Review review = Review.builder()
							  .rvwScore(reviewDTO.getRvwScore())
							  .rvwWriter(customUser.getUsername())
							  .rvwRegdate(LocalDateTime.now())							 
							  .rvwContent(reviewDTO.getRvwContent())
							  .rvwReferNo(reviewDTO.getRvwReferNo())
							  .rvwType("class")
							  .rvwReserNo(reviewDTO.getRvwReserNo())
							  .build();
		
		reviewService.insertClassRvw(review);
		
	
	}
	
}


