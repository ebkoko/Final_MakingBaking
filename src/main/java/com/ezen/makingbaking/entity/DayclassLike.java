package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_DAYCLASS_LIKE")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
@IdClass(DayclassLikeId.class)
public class DayclassLike {		// 클래스 찜
	@Id
	private String userId;		// 회원 아이디
	@Id
	private int dayclassNo;		// 클래스 번호
	
}
