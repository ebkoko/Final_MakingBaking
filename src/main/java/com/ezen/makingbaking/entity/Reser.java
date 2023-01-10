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
@Table(name="T_MB_RESER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class Reser {
	@Id
	private long reserNo;									// 예약번호
	private String userId;									// 회원 아이디
	private LocalDateTime reserDate = LocalDateTime.now();	// 예약일
	@Builder.Default
	private String reserStatus = "MV";						// 예약상태(입금대기: MV, 결제완료: PE, 예약취소: RC, 결제취소: PC)
	private String partiName;								// 예약자명
	private String partiTel;								// 예약자 전화번호
	private String partiTime;								// 예약시간
	private String partiDate;								// 예약날짜
	private int classNo;									// 클래스번호
	private int reserPersonCnt;								// 예약인원수
	private String orderName;								// 주문자명
	private String orderTel;								// 주문자 전화번호
	@Nullable
	private String request;									// 요청사항
	private String reserPayment;							// 결제방법(무통장입금, 카카오페이)
	@Nullable
	private String depositor;								// 무통장입금 입금자명
	private int reserTotalPrice;							// 총 결제금액
	private int classPrice;									// 클래스가격	
	@Builder.Default
	private String partiStatus = "PS";						// 참여현황(진행예정: PS, 진행완료: PC, 노쇼: NS)
	@Transient
	private String searchCondition;
	@Transient
	private String searchKeyword;
	private LocalDateTime reserCancelDate;					// 취소일
	private String tid;
}
