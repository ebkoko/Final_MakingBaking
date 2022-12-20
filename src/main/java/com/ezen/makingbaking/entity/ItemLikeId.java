package com.ezen.makingbaking.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ItemLikeId implements Serializable {
	private String userId;
	private int itemNo;
}
