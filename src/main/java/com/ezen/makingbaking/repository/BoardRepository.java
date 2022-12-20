package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {

}
