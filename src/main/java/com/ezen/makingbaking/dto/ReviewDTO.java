package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDTO {
	private int rvwNo;			// 리뷰번호
	private int rvwReferNo;		// 참조항목번호
	private String rvwType;		// 타입(item, class)
	private String rvwContent;	// 리뷰내용
	private String rvwWriter;	// 리뷰 작성자
	private String rvwRegdate;	// 리뷰작성일	
	private int rvwScore;
	private String searchCondition;
	private String searchKeyword;
	private long rvwOrderNo;
	private long rvwReserNo;
}
