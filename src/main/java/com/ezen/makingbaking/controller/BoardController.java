package com.ezen.makingbaking.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
	public ModelAndView getQnaList(BoardDTO boardDTO, @PathVariable("cateCode") int cateCode,
			@PageableDefault(page=0, size=10) Pageable pageable) {
		System.out.println(cateCode);
				
		Board board = Board.builder()
						   .cateCode(cateCode)
						   .build();
		
		List<Board> qnaList = boardService.getQnaList(board);
		
		Page<Board> pageBoardList = boardService.getPageBoardList(board, pageable);
		
		Page<BoardDTO> pageBoardDTOList = pageBoardList.map(pageQna -> 
						   BoardDTO.builder()
						   		   .boardNo(pageQna.getBoardNo())
						   		   .boardTitle(pageQna.getBoardTitle())
						   		   .boardContent(pageQna.getBoardContent())
						   		   .boardWriter(pageQna.getBoardWriter())
						   		   .boardRegdate(
						   				   		pageQna.getBoardRegdate() == null?
												null :
												pageQna.getBoardRegdate().toString())
						   		   .boardCnt(pageQna.getBoardCnt())
						   		   .build()
						   );  //화살표함수 람다식
		
		// 화면으로 보낼 때는 boardDTO로 보내줘야함
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
		mv.addObject("getQnaList", pageBoardDTOList); // content부터 pageable까지만 오게됨
		
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