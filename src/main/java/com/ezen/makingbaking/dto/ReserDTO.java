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
	private int reserNo;
	private int classNo;
	private String userId;
	private String reserName;
	private String reserDate;
	private String reserTime;
	private int reserPersonCnt;
	private String reserStatus;
}
