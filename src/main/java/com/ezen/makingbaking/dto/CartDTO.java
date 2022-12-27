package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
	private int cartNo;
	private int itemNo;
	private String userId;
	private int cartItemCnt;
	private char cartStatus;
}
