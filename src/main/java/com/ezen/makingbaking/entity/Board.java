package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_BOARD")
@Data
@SequenceGenerator(
		name="BoardSequenceGenerator",
		sequenceName="BOARD_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor //기본 생성자
@AllArgsConstructor //모든 매개변수를 받는 생성자
@Builder //객체 
@DynamicInsert // insert 구문이 생성 될 때 null값인 컬럼은 배제하고 구문생성
@DynamicUpdate // update 변경되지 않는 값들을 제외하고 구문생성
public class Board {	// Q&A(+답변), 공지사항, 이벤트 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="BoardSequenceGenerator"
	)
	private int boardNo;	// 글번호
	private int cateCode;	// 카테고리코드(CATEGORY)
	private String boardTitle;	// 글제목
	private String boardContent;	// 글내용
	private String boardWriter;		// 작성자
	private LocalDateTime boardRegdate = LocalDateTime.now();	// 작성일
	private int boardCnt;	// 조회수
	
	@Nullable
	private String boardReply;	// 답변(Q&A)
	@Nullable
	private LocalDateTime boardReplyRegdate = LocalDateTime.now();	// 답변작성일
	
}
