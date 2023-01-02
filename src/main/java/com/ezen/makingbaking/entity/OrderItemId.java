package com.ezen.makingbaking.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderItemId implements Serializable {
	private long orderNo;
	private int itemNo;
}
