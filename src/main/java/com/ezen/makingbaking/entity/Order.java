package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ORDER")
@Data
@SequenceGenerator(
		name="OrderSequenceGenerator",
		sequenceName="Order_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
public class Order {	// 주문정보 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="OrderSequenceGenerator"
	)
	private int orderNo;		// 주문번호
	private String userId;		// 주문회원 아이디
	private LocalDateTime orderDate = LocalDateTime.now();	// 주문일
	private String orderStatus;		// 주문상태(입금대기,결제완료 등..)
	private String orderName;		// 수령인명
	private String orderTel;		// 전화번호
	private String shippingAddr1;	// 배송주소(우편번호)
	private String shippingAddr2;	// 기본주소
	private String shippingAddr3;	// 상세주소
	private int orderDeliFee;		// 배송비
	private int orderTotalPrice;	// 총 가격	
	

}
