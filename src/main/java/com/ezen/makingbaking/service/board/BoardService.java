package com.ezen.makingbaking.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.makingbaking.entity.Board;

public interface BoardService {

	Page<Board> getPageBoardList(Board board, Pageable pageable); //Page<Board> 엔티티로 리턴해줌

	void updateBoardCnt(int boardNo);

	Board getBoard(int boardNo);


}
