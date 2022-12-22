package com.ezen.makingbaking.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_ADDRESS")
@Data
@SequenceGenerator(
		name="AddressSequenceGenerator",
		sequenceName="Address_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert
public class Address {		// 주소 테이블(한 회원이 여러 개의 주소를 쓸 수 있음)
	@Id
	private String userId;	// 아이디
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="AddressSequenceGenerator"
	)
	private int addressNo;		// 주소번호
	private String address1;	// 우편번호
	private String address2;	// 기본주소
	private String address3;	// 상세주소
	@Column
	@ColumnDefault("'Y'")
	private char addressUseYn;	// 주소사용여부(Y/N)

}
