package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
	private int orderNo;
	private String userId;
	private String orderDate;
	private String orderStatus;
	private String orderName;
	private String orderTel;
	private String shippingAddr1;
	private String shippingAddr2;
	private String shippingAddr3;
	private int orderDeliFee;
	private int orderTotalPrice;
	private String orderPayment;
}
