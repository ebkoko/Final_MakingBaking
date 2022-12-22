package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor	// 기본 생성자
@AllArgsConstructor	// 모든 매개변수를 받는 생성자
@Builder	// 객체 생성
public class BoardDTO {
	private int boardNo;
	private int cateCode;
	private String boardTitle;
	private String boardContent;
	private String boardWriter;
	private String boardRegdate;
	private int boardCnt;
	
	private String boardReply;
	private String boardReplyRegdate;

}
