package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.DayclassDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.entity.Dayclass;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.service.dayclass.DayclassService;
import com.ezen.makingbaking.service.review.ReviewService;

@RestController
@RequestMapping("/dayclass")
public class DayclassController {
	@Autowired
	private DayclassService dayclassService;
	
	@Autowired
	private ReviewService reviewService;
	
	@GetMapping("/dayclass/{dayclassNo}")
	public ModelAndView getDayclass(@PathVariable int dayclassNo, @PageableDefault(page = 0, size = 4) Pageable pageable) {
		
		Dayclass dayclass = dayclassService.getDayclass(dayclassNo);
		
		DayclassDTO dayclassDTO = DayclassDTO.builder()
											 .dayclassNo(dayclass.getDayclassNo())
											 .dayclassName(dayclass.getDayclassName())
											 .dayclassMinName(dayclass.getDayclassMinName())
											 .dayclassPrice(dayclass.getDayclassPrice())
											 .dayclassDetails(dayclass.getDayclassDetails())
											 .dayclassTime(dayclass.getDayclassTime())
											 .dayclassUseYn(dayclass.getDayclassUseYn())
											 .dayclassAddress(dayclass.getDayclassAddress())
											 .build();
		
		Page<Review> reviewList = reviewService.getReviewList(dayclassNo, pageable);
		
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
		
	
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("dayclass/getDayclass.html");
		
		mv.addObject("dayclass", dayclassDTO);
		mv.addObject("reviewList", reviewDTOList);
		return mv;
	}
	
	
	
	//
}