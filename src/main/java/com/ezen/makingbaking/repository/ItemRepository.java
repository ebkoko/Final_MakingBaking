package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.makingbaking.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

}
