package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DayclassDTO {
	private int dayclassNo;		// 클래스번호
	private String dayclassName;		// 클래스명
	private int dayclassPrice;			// 클래스가격
	private String dayclassDetails;		// 클래스 상세내용
	private char dayclassTime;			// 클래스운영시간(A(AM),P(PM),B(BOTH))
	private char dayclassUseYn;		// 진행상태(Y/N)
	private String dayclassAddress;		// 가게주소(고정값, 지도api 사용하기 위해 넣음)
}
