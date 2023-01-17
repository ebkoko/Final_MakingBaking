package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_ORDER")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
public class Order {	// 주문정보 테이블
	@Id
	private long orderNo;		// 주문번호
	private String userId;		// 주문회원 아이디
	private LocalDateTime orderDate = LocalDateTime.now();	// 주문일
	@Builder.Default
	private String orderStatus = "MV";		// 주문상태(입금대기: MV, 결제완료: PE, 주문취소: OC, 결제취소: PC, 배송준비중: DW, 배송중: D, 배송완료: DC)
	private String reciName;		// 수령인명
	private String reciTel;		// 전화번호
	private String shippingAddr1;	// 배송주소(우편번호)
	private String shippingAddr2;	// 기본주소
	private String shippingAddr3;	// 상세주소
	private int orderDeliFee;		// 배송비
	private int orderTotalPrice;	// 총 가격	
	private String orderPayment;    // 결제방법(무통장입금, 카카오페이)
	private String orderName;		// 주문자명
	private String orderTel;		// 주문자 전화번호
	@Nullable
	private String orderMessage;	// 배송메세지
	@Nullable
	private String orderMail;		// 주문자 이메일
	@Nullable
	private String depositor;		// 무통장입금 입금자명
	private int orderTotalPayPrice; // 배송비 포함 총 결제가격
	@Transient
	private String searchCondition;
	@Transient
	private String searchKeyword;
	private LocalDateTime orderCancelDate;	// 취소일
	private String tid;
}
