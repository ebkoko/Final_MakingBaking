package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
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
@Table(name="T_MB_USER")
@Data
@SequenceGenerator(
		name="UserSequenceGenerator",
		sequenceName="User_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
public class User {		// 회원 테이블
	@Id
	private String userId;	// 아이디
	private String userPw;	// 비밀번호
	private String userName;	// 회원이름
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="UserSequenceGenerator"
	)
	private int userNo;		// 회원번호
	private String userBirth;	// 회원생일
	private char userGender;	// 회원성별
	private String userTel;		// 전화번호
	private String userMail;	// 이메일
	private String userAddr1;	// 우편번호
	private String userAddr2;	// 기본주소
	private String userAddr3;	// 상세주소
	@Builder.Default
	private LocalDateTime userRegdate = LocalDateTime.now(); // 가입일
	@Column
	@ColumnDefault("ROLE_USER")
	private String userRole;	
	@Transient
	private String joinYn;
	@Transient
	private String searchCondition;
	@Transient
	private String searchKeyword;
}