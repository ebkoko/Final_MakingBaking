package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="FILE")
@Data
@SequenceGenerator(
		name="FileSequenceGenerator",
		sequenceName="FILE_SEQ",
		initialValue=1,
		allocationSize=1
)
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
@IdClass(FileId.class)
public class File {		// 상품, 클래스, 리뷰에서 쓰는 파일 테이블
	@Id
	@GeneratedValue(
			strategy=GenerationType.SEQUENCE,
			generator="FileSequenceGenerator"
	)
	private int fileNo;		// 파일번호
	@Id
	private int fileReferNo;		// 참조항목번호(상품번호, 클래스번호, 리뷰글번호)
	@Id
	private String fileType;		// 타입(item, class, review)
	private String fileName;		// 파일명
	private String fileOriginName;		// 원본파일명
	private String filePath;		// 파일 경로

}
