package com.ezen.makingbaking.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class CartId implements Serializable {
	private int cartNo;
	private int itemNo;
}
