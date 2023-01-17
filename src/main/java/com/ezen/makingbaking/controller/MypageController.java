package com.ezen.makingbaking.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.BoardDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.ReviewDTO;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.Review;
import com.ezen.makingbaking.service.board.BoardService;
import com.ezen.makingbaking.service.dayclass.DayclassService;
import com.ezen.makingbaking.service.mypage.MypageService;
import com.ezen.makingbaking.service.order.OrderService;
import com.ezen.makingbaking.service.reser.ReserService;
import com.ezen.makingbaking.service.review.ReviewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/mypage")
public class MypageController {
	@Autowired
	private DayclassService dayclassService;
	
	@Autowired
	private ReserService reserService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BoardService boardService;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MypageService mypageService;
	
	@GetMapping("/myPage")
	public ModelAndView myOrderList(OrderDTO orderDTO, @PageableDefault(page = 0, size = 5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ModelAndView mv = new ModelAndView();
		
		Page<CamelHashMap> orderList = orderService.getOrderList(customUser.getUsername(), pageable, orderDTO.getOrderCondition());
		
		List<CamelHashMap> orderContent = orderService.getOrderContent(customUser.getUsername());
		
		mv.addObject("getOrderList", orderList);
		mv.addObject("item", orderContent);
		mv.setViewName("mypage/myPage.html");
		return mv;
	}
	
	@GetMapping("/changeInfo")
	public ModelAndView changeInfoView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/changeInfo.html");
		return mv;
	}
	
	@GetMapping("/quit")
	   public ModelAndView quitView(HttpServletRequest request) {
	      ModelAndView mv = new ModelAndView();
	      mv.setViewName("mypage/quit.html");
	      if(request.getParameter("quitMsg") != null && !request.getParameter("quitMsg").equals("")) {
	         mv.addObject("quitMsg", request.getParameter("quitMsg").toString());
	      }
	      return mv;
	   }
	
	@GetMapping("/newPw")
	public ModelAndView newPwView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/newPw.html");
		return mv;
	}
	
	@GetMapping("/checkPwForm")
	public ModelAndView checkPwFormView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/checkPwForm.html");
		return mv;
	}
	
	@GetMapping("/getOrderDetail/{orderNo}")
	public ModelAndView orderDetailView(@PathVariable long orderNo, @AuthenticationPrincipal CustomUserDetails customUser) {
		CamelHashMap order = orderService.getOrderDetail(orderNo);
		
		List<CamelHashMap> orderContent = orderService.getOrderItem(orderNo);
		
		ModelAndView mv = new ModelAndView();
		
		order.put("order_Date_Str", (Timestamp.valueOf(order.get("orderDate").toString())).toLocalDateTime().toString());
		
		mv.addObject("order", order);
		mv.addObject("item", orderContent);
		mv.setViewName("mypage/getOrderDetail.html");
		return mv;
	}
	
	@GetMapping("/myReserList")
	public ModelAndView myReserList(ReserDTO reserDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ModelAndView mv = new ModelAndView();
		
		Page<CamelHashMap> reserList = reserService.getReserList(customUser.getUsername(), pageable, reserDTO.getReserCondition());
		
		
		mv.addObject("getReserList", reserList);		
		mv.setViewName("mypage/myReserList.html");
		return mv;
	}
	
	@GetMapping("/getReserDetail/{reserNo}")
	public ModelAndView reserDetailView(@PathVariable long reserNo, @AuthenticationPrincipal CustomUserDetails customUser) {
		CamelHashMap reser = reserService.getReserDetail(reserNo);
		
		ModelAndView mv = new ModelAndView();
		
		reser.put("reser_Date_Str", ((Timestamp)reser.get("reserDate")).toLocalDateTime().toString());
		
		mv.addObject("reser", reser);
		mv.setViewName("mypage/getReserDetail.html");
		return mv;
	}
	
	@PostMapping("/myReserListAjax")
	public ResponseEntity<?> myReserListAjax(ReserDTO reserDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Page<CamelHashMap> reserList = reserService.getReserList(customUser.getUsername(), pageable, reserDTO.getReserCondition());
			response.setPageItems(reserList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	@PostMapping("/myPageAjax")
	public ResponseEntity<?> myPageAjax(OrderDTO orderDTO, @PageableDefault(page = 0, size = 5) Pageable pageable
			, @AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Page<CamelHashMap> orderList = orderService.getOrderList(customUser.getUsername(), pageable, orderDTO.getOrderCondition());
			response.setPageItems(orderList);
			
			for(CamelHashMap c : orderList.getContent()) {
				System.out.println("orderDate =================================" + c.get("orderDate"));
				c.put("order_Date_Str", c.get("orderDate").toString());
			}
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 내가 작성한 qna 글 목록 보여주기
	@GetMapping("/qnaList")
	public ModelAndView myQnaList(BoardDTO boardDTO, @PageableDefault(page=0, size=10) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
				
		Board board = Board.builder()
				   		   .boardWriter(customUser.getUsername())
						   .build();
		
		Page<Board> pageBoardList = boardService.getPageMyQnaList(board, pageable);
		
		Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageQna -> 
						   BoardDTO.builder()
						   		   .boardNo(pageQna.getBoardNo())
						   		   .boardTitle(pageQna.getBoardTitle())
						   		   .boardContent(pageQna.getBoardContent())
						   		   .boardWriter(pageQna.getBoardWriter())
						   		   .boardReply(pageQna.getBoardReply())
						   		   .boardRegdate(
						   				   		pageQna.getBoardRegdate() == null?
												null :
												pageQna.getBoardRegdate().toString())
						   		   .cateCode(pageQna.getCateCode())
						   		   .boardCnt(pageQna.getBoardCnt())
						   		   .build()
						   );
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/myQnaList.html");
		mv.addObject("getQnaList", pageBoardDTOList); 
		
		return mv;
	}
	
	// [ajax - 페이징] 내가 작성한 qna 글 목록 보여주기
	@PostMapping("/qnaList")
	public ResponseEntity<?> getNoticePageList(@PageableDefault(page=0, size=10) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board board = Board.builder()
							   .boardWriter(customUser.getUsername())
							   .build();
			Page<Board> pageBoardList = boardService.getPageMyQnaList(board, pageable);
			
			Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageQna -> 
							   BoardDTO.builder()
									   .boardNo(pageQna.getBoardNo())
							   		   .boardTitle(pageQna.getBoardTitle())
							   		   .boardContent(pageQna.getBoardContent())
							   		   .boardWriter(pageQna.getBoardWriter())
							   		   .boardReply(pageQna.getBoardReply())
							   		   .boardRegdate(
							   				   		pageQna.getBoardRegdate() == null?
													null :
													pageQna.getBoardRegdate().toString())
							   		   .cateCode(pageQna.getCateCode())
							   		   .boardCnt(pageQna.getBoardCnt())
							   		   .build()
							   );  
			response.setPageItems(pageBoardDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	// 나의 리뷰
	@GetMapping("/myRvwList")
	public ModelAndView myRvwList(ReviewDTO reviewDTO, @PageableDefault(page=0, size=4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
				
	
	
		Page<CamelHashMap> pageReviewList = reviewService.getPageMyRvwList(customUser.getUsername(), pageable);
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/myPageRvw.html");
		mv.addObject("pageReviewList", pageReviewList); 
		
		return mv;
	}
	
	// 나의 리뷰 페이징
	@PostMapping("/myRvwList")
	public ResponseEntity<?> myRvwPageList(@PageableDefault(page=0, size=4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			
			Page<CamelHashMap> pageReviewList = reviewService.getPageMyRvwList(customUser.getUsername(), pageable);
			
			 
			response.setPageItems(pageReviewList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	// 나의 리뷰 삭제
	@PostMapping("/deleteMyRvw")
	public ResponseEntity<?> deleteRvwList(@RequestParam("changeRows") String changeRows,
			@PageableDefault(page = 0, size = 4) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) throws JsonMappingException, JsonProcessingException {
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		List<Map<String, Object>> changeRowsList = new ObjectMapper().readValue(changeRows, 
											new TypeReference<List<Map<String, Object>>>() {});
		
		try {
			reviewService.deleteRvw(changeRowsList);
			
		
			Page<CamelHashMap> pageReviewList = reviewService.getPageMyRvwList(customUser.getUsername(), pageable);
			
			response.setPageItems(pageReviewList);
			
			return ResponseEntity.ok().body(response);
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	
	
	// 나의 찜 목록
	@GetMapping("/myLikeList")
	public ModelAndView myLikeList(@PageableDefault(page=0, size=5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
	
		Page<CamelHashMap> pageLikeList = mypageService.getPageMyLikeList(customUser.getUsername(), pageable);

		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/myPageLike.html");
		mv.addObject("pageLikeList", pageLikeList); 
		
		return mv;
	}
	
	// [ajax - 페이징] 나의 찜 목록
	@PostMapping("/myLikeList")
	public ResponseEntity<?> myLikePageList(@PageableDefault(page=0, size=5) Pageable pageable,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		
		ResponseDTO<CamelHashMap> response = new ResponseDTO<>();
		
		try {
			Page<CamelHashMap> pageLikeList = mypageService.getPageMyLikeList(customUser.getUsername(), pageable);
			
			response.setPageItems(pageLikeList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 찜 해제
	@DeleteMapping("/mypageUnlike")
	public void mypageUnlike(@RequestParam("likeNo") int likeNo, 
			@RequestParam("cateName") String cateName,
			@AuthenticationPrincipal CustomUserDetails customUser) {
		
		System.out.println(cateName + "@@@@@@@@@@@@" + likeNo);
		
		if(cateName.equals("class")) {
			mypageService.classUnlike(likeNo, customUser.getUsername());
			System.out.println("classUnlike");
		} else if(cateName.equals("item")) {
			mypageService.itemUnlike(likeNo, customUser.getUsername());
			System.out.println("itemUnlike");
		}
	}
 
	
}
