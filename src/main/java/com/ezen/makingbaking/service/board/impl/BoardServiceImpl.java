package com.ezen.makingbaking.service.board.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.Board;
import com.ezen.makingbaking.repository.BoardRepository;
import com.ezen.makingbaking.service.board.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardRepository boardRepository;

	@Override
	public List<Board> getQnaList(Board board) {
		return boardRepository.findByCateCode(board.getCateCode());
	}


}
