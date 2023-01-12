package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_REVIEW")
@Data
@SequenceGenerator(
		name="ReviewSequenceGenerator",
		sequenceName="REVIEW_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
public class Review {		// 리뷰 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="ReviewSequenceGenerator"
	)
	private int rvwNo;			// 리뷰번호
	private int rvwReferNo;		// 참조항목번호
	private String rvwType;		// 타입(item, class)
	private String rvwContent;	// 리뷰내용
	private String rvwWriter;	// 리뷰 작성자
	private LocalDateTime rvwRegdate = LocalDateTime.now();	// 리뷰작성일	
	@Builder.Default
	private int rvwScore = 5;	// 별점
	private long rvwOrderNo;
	private long rvwReserNo;
	@Transient
	private String searchCondition;
	@Transient
	private String searchKeyword;
	

	// 1227 수정 - rvwTitle 삭제, rvwScore 추가
	
}
