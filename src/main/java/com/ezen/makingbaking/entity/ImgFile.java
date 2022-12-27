package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="T_MB_FILE")
@Data
@NoArgsConstructor 
@AllArgsConstructor 
@Builder
@DynamicInsert 
@IdClass(FileId.class)
public class ImgFile {		// 상품, 클래스, 리뷰에서 쓰는 파일 테이블
	
	@Id
	private int fileNo;		// 파일번호
	@Id
	private int fileReferNo;		// 참조항목번호(상품번호, 클래스번호, 리뷰글번호)
	@Id
	private String fileType;		// 타입(item, class, review)
	private String fileName;		// 파일명
	private String fileOriginName;		// 원본파일명
	private String filePath;		// 파일 경로
}
