package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_ITEM_LIKE")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
@IdClass(ItemLikeId.class)
public class ItemLike {		// 상품 찜 테이블
	@Id
	private String userId;		// 회원 아이디
	@Id
	private int itemNo;		// 아이템 번호
}
