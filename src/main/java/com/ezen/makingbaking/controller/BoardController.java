package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

import com.ezen.makingbaking.dto.BoardDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.service.board.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	// qna 글 목록 보여주기
	@GetMapping("/qnaList/{cateCode}")
	public ModelAndView getQnaList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		System.out.println(cateCode);
				
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .boardNo(boardDTO.getBoardNo())
						   .build();
		
		List<Board> faqList = boardService.getFaqList(board);
		
		List<BoardDTO> getFaqList = new ArrayList<BoardDTO>();
		for(int i=0; i<faqList.size(); i++) {
			BoardDTO returnFaq = BoardDTO.builder()
										   .boardNo(faqList.get(i).getBoardNo())
										   .boardTitle(faqList.get(i).getBoardTitle())
										   .boardContent(faqList.get(i).getBoardContent())
										   .boardWriter(faqList.get(i).getBoardWriter())
										   .boardReply(faqList.get(i).getBoardReply())
										   .build();
			getFaqList.add(returnFaq);
		}
		
		Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
		
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
						   		   .boardCnt(pageQna.getBoardCnt())
						   		   .build()
						   );
		
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getQnaList.html");
		mv.addObject("getFaqList", getFaqList);
		mv.addObject("getQnaList", pageBoardDTOList); 
		
		return mv;
	}
	
	// qna - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/qnaList/{cateCode}")
	public ResponseEntity<?> getQnaPageList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board board = Board.builder()
							   .cateCode(cateCode)
							   .boardNo(boardDTO.getBoardNo())
							   .build();
			Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
			
			Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageQna -> 
							   BoardDTO.builder()
							   		   .boardNo(pageQna.getBoardNo())
							   		   .boardTitle(pageQna.getBoardTitle())
							   		   .boardContent(pageQna.getBoardContent())
							   		   .boardReply(pageQna.getBoardReply())
							   		   .boardWriter(pageQna.getBoardWriter())
							   		   .boardRegdate(
							   				   		pageQna.getBoardRegdate() == null?
													null :
													pageQna.getBoardRegdate().toString())
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
	
	// 공지사항 글 목록 보여주기
	@GetMapping("/noticeList/{cateCode}")
	public ModelAndView getNoticeList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		System.out.println(cateCode);
				
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .build();
		
		Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
		
		Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageNotice -> 
						   BoardDTO.builder()
						   		   .boardNo(pageNotice.getBoardNo())
						   		   .boardTitle(pageNotice.getBoardTitle())
						   		   .boardContent(pageNotice.getBoardContent())
						   		   .boardWriter(pageNotice.getBoardWriter())
						   		   .boardRegdate(
						   				   pageNotice.getBoardRegdate() == null?
												null :
												pageNotice.getBoardRegdate().toString())
						   		   .boardCnt(pageNotice.getBoardCnt())
						   		   .build()
						   ); 
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getNoticeList.html");
		mv.addObject("getBoardList", pageBoardDTOList); 
		
		return mv;
	}
	
	// 공지사항 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/noticeList/{cateCode}")
	public ResponseEntity<?> getNoticePageList(@PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board board = Board.builder()
							   .cateCode(cateCode)
							   .build();
			Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
			
			Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageNotice -> 
							   BoardDTO.builder()
							   		   .boardNo(pageNotice.getBoardNo())
							   		   .boardTitle(pageNotice.getBoardTitle())
							   		   .boardContent(pageNotice.getBoardContent())
							   		   .boardWriter(pageNotice.getBoardWriter())
							   		   .boardRegdate(
							   				   		pageNotice.getBoardRegdate() == null?
													null :
													pageNotice.getBoardRegdate().toString())
							   		   .boardCnt(pageNotice.getBoardCnt())
							   		   .build()
							   );  
			response.setPageItems(pageBoardDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// 이벤트 글 목록 보여주기
	@GetMapping("/eventList/{cateCode}")
	public ModelAndView getEventList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		System.out.println(cateCode);
				
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .build();
		
		Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
		
		Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageEvent -> 
						   BoardDTO.builder()
						   		   .boardNo(pageEvent.getBoardNo())
						   		   .boardTitle(pageEvent.getBoardTitle())
						   		   .boardContent(pageEvent.getBoardContent())
						   		   .boardWriter(pageEvent.getBoardWriter())
						   		   .boardRegdate(
						   				    pageEvent.getBoardRegdate() == null ?
											null :
											pageEvent.getBoardRegdate().toString())
						   		   .boardCnt(pageEvent.getBoardCnt())
						   		   .build()
						   ); 
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getEventList.html");
		mv.addObject("getBoardList", pageBoardDTOList); 
		
		return mv;
	}
	
	// 이벤트 - ajax로 처리한 페이징 글 목록 보여주기
	@PostMapping("/eventList/{cateCode}")
	public ResponseEntity<?> getEventPageList(@PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board board = Board.builder()
							   .cateCode(cateCode)
							   .build();
			Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
			
			Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageEvent -> 
							   BoardDTO.builder()
							   		   .boardNo(pageEvent.getBoardNo())
							   		   .boardTitle(pageEvent.getBoardTitle())
							   		   .boardContent(pageEvent.getBoardContent())
							   		   .boardWriter(pageEvent.getBoardWriter())
							   		   .boardRegdate(
							   				   		pageEvent.getBoardRegdate() == null?
													null :
													pageEvent.getBoardRegdate().toString())
							   		   .boardCnt(pageEvent.getBoardCnt())
							   		   .build()
							   );  
			response.setPageItems(pageBoardDTOList);
			
			return ResponseEntity.ok().body(response);
			
		} catch(Exception e) {
			response.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}
	
	// qna 글 조회수 증가 -> qna 글 상세보기로 이동
	@GetMapping("/updateQnaCnt/{boardNo}")
	public void updateQnaCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/qna/" + boardNo);
	}
	
	// 공지사항 글 조회수 증가 -> 공지사항 글 상세보기로 이동
	@GetMapping("/updateNoticeCnt/{boardNo}")
	public void updateNoticeCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/notice/" + boardNo);	
	}
	
	// 이벤트 글 조회수 증가 -> 이벤트 글 상세보기로 이동
	@GetMapping("/updateEventCnt/{boardNo}")
	public void updateEventCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/event/" + boardNo);	
	}
	
	// qna 글 상세보기
	@GetMapping("/qna/{boardNo}")
	public ModelAndView getQna(@PathVariable int boardNo, 
			@AuthenticationPrincipal CustomUserDetails customUser){
		Board board = boardService.getBoard(boardNo);
		
		String loginUserId = "";
		
		if(customUser != null)
			loginUserId = customUser.getUser().getUserId();
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardReply(board.getBoardReply())
									.boardRegdate(
												board.getBoardRegdate() == null?
												null :
												board.getBoardRegdate().toString()) // .toString 수정 
									.boardCnt(board.getBoardCnt())
									.cateCode(board.getCateCode())
									.build();	
				
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getQna.html");
		mv.addObject("getBoard", boardDTO);
		mv.addObject("userId", loginUserId);
		return mv;
	}
	
	// 공지사항 글 상세보기
	@GetMapping("/notice/{boardNo}")
	public ModelAndView getNotice(@PathVariable int boardNo){
		Board board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardRegdate(
												board.getBoardRegdate() == null?
												null :
												board.getBoardRegdate().toString()) // .toString 수정 
									.boardCnt(board.getBoardCnt())
									.cateCode(board.getCateCode())
									.build();	
				
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoard.html");
		mv.addObject("getBoard", boardDTO);

		return mv;
	}
	
	// 이벤트 글 상세보기
	@GetMapping("/event/{boardNo}")
	public ModelAndView getEvent(@PathVariable int boardNo){
		Board board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardRegdate(
												board.getBoardRegdate() == null?
												null :
												board.getBoardRegdate().toString()) // .toString 수정 
									.boardCnt(board.getBoardCnt())
									.cateCode(board.getCateCode())
									.build();	
				
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getBoard.html");
		mv.addObject("getBoard", boardDTO);

		return mv;
	}
	
	// [user] qna 질문 글 작성 페이지 이동
	@GetMapping("/qna/insertQna/{cateCode}")
	public ModelAndView insertQnaView(@PathVariable("cateCode") int cateCode, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		System.out.println(customUser.getUsername());
		ModelAndView mv = new ModelAndView();
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/insertQna.html");
		return mv;
	}

	// [admin] getQna에서 user가 작성한 질문 데이터를 가지고 답글 작성 페이지로 이동
	@GetMapping("/qna/insertAnswer/{cateCode}/{boardNo}")
	public ModelAndView insertAnswerView(@PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.boardReply(board.getBoardReply())
									.boardRegdate(
												board.getBoardRegdate() == null?
												null :
												board.getBoardRegdate().toString())
									.boardCnt(board.getBoardCnt())
									.cateCode(board.getCateCode())
									.build();
				
		ModelAndView mv = new ModelAndView();
		mv.addObject("getBoard", boardDTO);		
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/insertAnswer.html");
		return mv;
	}
	
	// [admin] 공지사항 작성 페이지 이동
	@GetMapping("/notice/insertNotice/{cateCode}")
	public ModelAndView insertNoticeView(@PathVariable("cateCode") int cateCode, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		System.out.println(customUser.getUsername());
		ModelAndView mv = new ModelAndView();
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/insertNotice.html");
		return mv;
	}
	
	// [admin] 이벤트 글 작성 페이지 이동
	@GetMapping("/event/insertEvent/{cateCode}")
	public ModelAndView insertEventView(@PathVariable("cateCode") int cateCode, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		System.out.println(customUser.getUsername());
		ModelAndView mv = new ModelAndView();
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/insertEvent.html");
		return mv;
	}
	
	// [user] qna 글 작성
	@PostMapping("/qna/{cateCode}")
	public void insertQna(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardTitle(boardDTO.getBoardTitle())
						   .boardContent(boardDTO.getBoardContent())
						   .boardWriter(customUser.getUsername())
						   .boardRegdate(LocalDateTime.now())
						   .cateCode(boardDTO.getCateCode())
						   .build();
		
		boardService.insertBoard(board);
		
		response.sendRedirect("/board/qnaList/" + cateCode);
	}
	
	// [admin] qna 상세페이지에서 답글 작성
	@PostMapping("/qna/answer/{cateCode}/{boardNo}")
	public void insertAnswer(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardNo(boardDTO.getBoardNo())
						   .boardReply(boardDTO.getBoardContent())
						   .boardReplyRegdate(LocalDateTime.now())
						   .cateCode(cateCode)
						   .build();
		
		boardService.updateAnswer(board);
		
		response.sendRedirect("/board/qna/" + boardNo);
	}
	
	// [admin] 공지사항 글 작성
	@PostMapping("/notice/{cateCode}")
	public void insertNotice(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardTitle(boardDTO.getBoardTitle())
						   .boardContent(boardDTO.getBoardContent())
						   .boardWriter(customUser.getUsername())
						   .boardRegdate(LocalDateTime.now())
						   .cateCode(cateCode)
						   .build();
		
		boardService.insertBoard(board);
		
		response.sendRedirect("/board/noticeList/" + cateCode);
	}
	
	// [admin] 이벤트 글 작성
	@PostMapping("/event/{cateCode}")
	public void insertEvent(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardTitle(boardDTO.getBoardTitle())
						   .boardContent(boardDTO.getBoardContent())
						   .boardWriter(customUser.getUsername())
						   .boardRegdate(LocalDateTime.now())
						   .cateCode(cateCode)
						   .build();
		
		boardService.insertBoard(board);
		
		response.sendRedirect("/board/eventList/" + cateCode);
	}
	
	// [user] qna 글 삭제(admin 답변이 없는 경우만 삭제 가능)
	@DeleteMapping("/qna")
	public void deleteQna(@RequestParam("boardNo") int boardNo) {
		boardService.deleteBoard(boardNo);
	}
	
	// [admin] 공지사항, 이벤트 글 삭제
	@DeleteMapping("/board")
	public void deleteBoard(@RequestParam("boardNo") int boardNo) {
		boardService.deleteBoard(boardNo);
	}
	
	// [user] qna 질문 글 수정 페이지 이동
	@GetMapping("/qna/updateQna/{cateCode}/{boardNo}")
	public ModelAndView updateQnaView(@PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.cateCode(board.getCateCode())
									.build();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("getBoard", boardDTO);		
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/updateQna.html");
		return mv;
	}
	
	// [user] qna 수정페이지에서 글 수정
	@PostMapping("/qna/{cateCode}/{boardNo}")
	public void updateQna(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardNo(boardDTO.getBoardNo())
						   .boardTitle(boardDTO.getBoardTitle())
						   .boardContent(boardDTO.getBoardContent())
						   .cateCode(boardDTO.getCateCode())
						   .build();
		
		boardService.updateQna(board);
		
		response.sendRedirect("/board/qna/" + boardNo);
	}
	
	// [admin] 공지사항, 이벤트 글 수정 페이지 이동
	@GetMapping("/board/updateBoard/{cateCode}/{boardNo}")
	public ModelAndView updateBoardView(@PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = boardService.getBoard(boardNo);
		
		BoardDTO boardDTO = BoardDTO.builder()
									.boardNo(board.getBoardNo())
									.boardTitle(board.getBoardTitle())
									.boardContent(board.getBoardContent())
									.boardWriter(board.getBoardWriter())
									.cateCode(board.getCateCode())
									.build();
		
		ModelAndView mv = new ModelAndView();
		mv.addObject("getBoard", boardDTO);		
		mv.addObject("cateCode", cateCode);
		mv.setViewName("board/updateBoard.html");
		return mv;
	}
	
	// [admin] 공지사항 수정페이지에서 글 수정
	@PostMapping("/board/{cateCode}/{boardNo}")
	public void updateBoard(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode, @PathVariable("boardNo") int boardNo, 
			HttpServletResponse response, HttpServletRequest request,
			@AuthenticationPrincipal CustomUserDetails customUser) throws IOException {
		Board board = Board.builder()
						   .boardNo(boardDTO.getBoardNo())
						   .boardTitle(boardDTO.getBoardTitle())
						   .boardContent(boardDTO.getBoardContent())
						   .cateCode(boardDTO.getCateCode())
						   .build();
		
		boardService.updateBoard(board);
		
		String categoryTitle = "";
		
		if(boardDTO.getCateCode() == 3) {
			categoryTitle = "notice";
		} else {
			categoryTitle = "event";
		}
		
		System.out.println("@@@@@@@@categoryTitle: " + categoryTitle);
		
		response.sendRedirect("/board/" + categoryTitle + "/" + boardNo);
	}
	
	
}