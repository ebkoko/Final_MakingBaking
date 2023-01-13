package com.ezen.makingbaking.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private String userId;
	private String userPw;
	private String userName;
	private int userNo;
	private String userBirth;
	private char userGender;
	private String userTel;
	private String userMail;
	private String userAddr1;
	private String userAddr2;
	private String userAddr3;
	private String userRegdate;
	private String searchCondition;
	private String searchKeyword;
}