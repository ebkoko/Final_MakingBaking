package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
	private long orderNo;
	private int itemNo;
	private int orderItemCnt;
	private int orderItemPrice;
}
