package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_RESER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class Reser {
	@Id
	private long reserNo;
	private String userId;
	private LocalDateTime reserDate = LocalDateTime.now();
	@Builder.Default
	private String reserStatus = "입금대기";
	private String partiName;
	private String partiTel;
	private String partiTime;
	private int classNo;
	private int reserPersonCnt;
	private String orderName;
	private String orderTel;
	@Nullable
	private String request;
	private String reserPayment;
	@Nullable
	private String depositor;
}
