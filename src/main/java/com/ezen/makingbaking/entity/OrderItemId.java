package com.ezen.makingbaking.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class OrderItemId implements Serializable {
	private int orderNo;
	private int itemNo;
}
