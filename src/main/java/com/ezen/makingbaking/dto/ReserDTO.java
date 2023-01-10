package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserDTO {
	private long reserNo;
	private String userId;
	private String reserDate;
	private String reserStatus;
	private String partiName;
	private String partiTel;
	private String partiTime;
	private String partiDate;
	private int classNo;
	private int reserPersonCnt;
	private String orderName;
	private String orderTel;
	private String request;
	private String reserPayment;
	private String depositor;
	private int reserTotalPrice;
	private int classPrice;
	private String partiStatus;
	private String searchCondition;
	private String searchKeyword;
	private String reserCancelDate;
	private String tid;
	private String reserCondition;
}
