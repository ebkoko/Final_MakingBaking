package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ITEM")
@Data
@SequenceGenerator(
		name="ItemSequenceGenerator",
		sequenceName="Item_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
public class Item {		// 상품 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="ItemSequenceGenerator"
	)
	private int itemNo;		// 상품번호
	private String itemName;	// 상품이름
	private int itemPrice;		// 상품가격
	private String itemDetails;		// 상품상세내용
	private int itemStock;		// 재고량
	@Column
	@ColumnDefault("'Y'")
	private char itemStatus;	// 판매상태(기본값 Y, 재고없음 S, 판매중지 N)
	private String itemCate;	// 카테고리(쿠키, 케잌..)
	private LocalDateTime itemRegdate = LocalDateTime.now();	// 상품추가일	

}
