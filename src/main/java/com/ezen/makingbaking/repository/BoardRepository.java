package com.ezen.makingbaking.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Board;

@Transactional
public interface BoardRepository extends JpaRepository<Board, Integer> {
	// 카테고리 코드로 리스트 검색
	Page<Board> findByCateCode(int cateCode, Pageable pageable);
	
	List<Board> findByCateCode(int cateCode);
	
}
