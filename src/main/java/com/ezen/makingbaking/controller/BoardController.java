package com.ezen.makingbaking.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.BoardDTO;
import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.service.board.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;
	
	@GetMapping("/qna/{cateCode}")
	public ModelAndView getQnaList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode) {
		System.out.println(cateCode);
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .build();
		List<Board> qnaList = boardService.getQnaList(board);
		
		List<BoardDTO> getQnaList = new ArrayList<BoardDTO>();
		for(int i=0; i<qnaList.size(); i++) {
			BoardDTO returnBoard = BoardDTO.builder()
										   .boardNo(qnaList.get(i).getBoardNo())
										   .cateCode(qnaList.get(i).getCateCode())
										   .boardTitle(qnaList.get(i).getBoardTitle())
										   .boardContent(qnaList.get(i).getBoardContent())
										   .boardWriter(qnaList.get(i).getBoardWriter())
										   .boardRegdate(
												   qnaList.get(i).getBoardRegdate() == null ?
												   null : 
											       qnaList.get(i).getBoardRegdate().toString())
										   .boardReply(qnaList.get(i).getBoardReply())
										   .boardCnt(qnaList.get(i).getBoardCnt())
										   .build();
			getQnaList.add(returnBoard);
		}
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getQnaList.html");
		mv.addObject("getQnaList", getQnaList);
		
		return mv;
	}
	
	
	@GetMapping("/notice")
	public ModelAndView noticeListView() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getNoticeList.html");
		return mv;
	}
	
	@GetMapping("/event")
	public ModelAndView eventListView() {
		
		ModelAndView mv = new ModelAndView();
		mv.setViewName("board/getEventList.html");
		return mv;
	}
	
}