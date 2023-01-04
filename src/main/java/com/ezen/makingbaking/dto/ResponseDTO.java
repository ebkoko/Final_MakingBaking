package com.ezen.makingbaking.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class ResponseDTO<T> {
	private List<T> items;
	
	private T item;
	
	private Page<T> pageItems;
	
	private String errorMessage;
	
	private int statusCode;


}
