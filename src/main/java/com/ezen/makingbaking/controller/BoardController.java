package com.ezen.makingbaking.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.BoardDTO;
import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.service.board.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/qnaList/{cateCode}")
	public ModelAndView getQnaList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		System.out.println(cateCode);
				
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .build();
		
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
		mv.addObject("getQnaList", pageBoardDTOList); // content부터 pageable까지만 오게됨
		
		return mv;
	}
	
	@PostMapping("/qnaList/{cateCode}")
	public ResponseEntity<?> getQnaPageList(@PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		ResponseDTO<BoardDTO> response = new ResponseDTO<>();
		try {
			Board board = Board.builder()
							   .cateCode(cateCode)
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
	
	@GetMapping("/updateQnaCnt/{boardNo}")
	public void updateQnaCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/qna/1" + boardNo);	
	}
	
	@GetMapping("/updateNoticeCnt/{boardNo}")
	public void updateNoticeCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/notice/3" + boardNo);	
	}
	
	@GetMapping("/updateEventCnt/{boardNo}")
	public void updateEventCnt(@PathVariable int boardNo, HttpServletResponse response) throws IOException {
		boardService.updateBoardCnt(boardNo);
		
		response.sendRedirect("/board/event/4" + boardNo);	
	}
	
	@GetMapping("/qna/{boardNo}")
	public ModelAndView getBoard(@PathVariable int boardNo){
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
												board.getBoardRegdate().toString()) // .toString 수정 
									.boardCnt(board.getBoardCnt())
									.build();	
				
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getQna.html");
		mv.addObject("getBoard", boardDTO);

		return mv;
	}
	
}