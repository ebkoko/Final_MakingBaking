package com.ezen.makingbaking.service.board.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.repository.BoardRepository;
import com.ezen.makingbaking.service.board.BoardService;

@Service
public class BoardServiceImpl implements BoardService {
	@Autowired
	private BoardRepository boardRepository;
}
