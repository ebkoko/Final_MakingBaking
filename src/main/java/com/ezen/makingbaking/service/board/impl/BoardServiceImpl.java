package com.ezen.makingbaking.service.board.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.repository.BoardRepository;
import com.ezen.makingbaking.service.board.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardRepository boardRepository;

	@Override
	public Page<Board> getPageBoardList(Board board, Pageable pageable) {
		return boardRepository.findByCateCodeOrderByBoardNoDesc(board.getCateCode(), pageable);
	}

	@Override
	public void updateBoardCnt(int boardNo) {
		boardRepository.updateBoardCnt(boardNo);		
	}

	@Override
	public Board getBoard(int boardNo) {
		return boardRepository.findById(boardNo).get();
	}

	@Override
	public void insertBoard(Board board) {
		boardRepository.save(board);		
	}

	@Override
	public void updateAnswer(Board board) {
		boardRepository.updateAnswer(board.getBoardNo(), board.getBoardReply());
	}

	@Override
	public List<Board> getFaqList(Board board) {
		return boardRepository.selectFaqList(board);
	}

	@Override
	public void deleteBoard(int boardNo) {
		boardRepository.deleteById(boardNo);		
	}

	@Override
	public void updateQna(Board board) {
		boardRepository.updateQna(board.getBoardTitle(), board.getBoardContent(), board.getCateCode(), board.getBoardNo());
	}

	@Override
	public void updateBoard(Board board) {
		boardRepository.updateBoard(board.getBoardTitle(), board.getBoardContent(), board.getBoardNo());
		
	}

	@Override
	public Page<Board> getPageMyQnaList(Board board, Pageable pageable) {
		return boardRepository.getMyQnaList(board.getBoardWriter(), pageable);
	}


	


}
