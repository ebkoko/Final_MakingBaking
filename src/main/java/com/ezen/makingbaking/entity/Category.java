package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_CATEGORY")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
public class Category {
	@Id
	private int cateCode;
	private String cateName;
	
}
