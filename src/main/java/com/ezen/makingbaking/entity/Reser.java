package com.ezen.makingbaking.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_RESER")
@Data
@SequenceGenerator(
		name="ReserSequenceGenerator",
		sequenceName="Reser_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class Reser {
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="ReserSequenceGenerator"
	)
	private int reserNo;
	private int classNo;
	private String userId;
	private String reserName;
	private LocalDateTime reserDate;
	private String reserTime;
	private int reserPersonCnt;
	private String reserStatus;
}
