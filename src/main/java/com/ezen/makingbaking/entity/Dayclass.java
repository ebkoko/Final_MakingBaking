package com.ezen.makingbaking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_DAYCLASS")
@Data
@SequenceGenerator(
		name="DayclassSequenceGenerator",
		sequenceName="Dayclass_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
public class Dayclass {		// 원데이클래스 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="DayclassSequenceGenerator"
	)
	private int dayclassNo;				// 클래스번호
	private String dayclassName;		// 클래스명
	private String dayclassMinName;		// 클래스 소제목
	private int dayclassPrice;			// 클래스가격
	private int dayclassDurationTime;	// 클래스 소요시간
	private String dayclassDetails;		// 클래스 상세내용
	private char dayclassTime;			// 클래스운영시간(A(AM),P(PM),B(BOTH))
	@Column
	@ColumnDefault("'Y'")	// 기본값 Y
	private char dayclassUseYn;			// 진행상태(Y/N/S)
	@Column
	@ColumnDefault("'서울 서초구 서초대로77길 54 서초더블유타워'")
	private String dayclassAddress;		// 가게주소(고정값, 지도api 사용하기 위해 넣음)
	@Transient
	private String searchCondition;
	@Transient
	private String searchKeyword;
	

}
