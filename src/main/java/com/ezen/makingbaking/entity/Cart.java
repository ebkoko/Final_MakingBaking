package com.ezen.makingbaking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_CART")
@Data
@SequenceGenerator(
		name="CartSequenceGenerator",
		sequenceName="Cart_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
@IdClass(CartId.class)
public class Cart {		// 장바구니 테이블	
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="CartSequenceGenerator"
	)
	private int cartNo;		// 카트번호
	@Id
	private int itemNo;		// 상품번호
	private String userId;		// 회원아이디
	private int cartItemCnt;	// 상품수량		
	@Builder.Default
	private char cartStatus = 'C';	// C : 현재 장바구니 아이템
									// D : 삭제된 장바구니 아이템
									// O : 주문한 장바구니 아이템	

}
