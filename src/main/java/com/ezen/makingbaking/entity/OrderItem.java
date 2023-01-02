package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_ORDER_ITEM")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
@IdClass(OrderItemId.class)
public class OrderItem {	// 주문아이템 테이블, ORDER 테이블에 들어가는 상품정보(+회원이 선택한 수량)
	@Id
	private long orderNo;	// 주문번호
	@Id
	private int itemNo;		// 상품번호
	private int orderItemCnt;	// 주문수량
	private int orderItemPrice;	// 상품가격
	
}
