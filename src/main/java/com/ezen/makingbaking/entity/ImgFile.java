package com.ezen.makingbaking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	private int fileReferNo;		// 참조항목번호(상품번호, 클래스번호, 리뷰글번호) ex)상품1
	@Id
	private int fileNo;				// 첨부파일번호 ex) (상품1의) 이미지1,  이미지2, 이미지3, ...
	@Id
	private String fileType;		// 타입(item, class, review)
	private String fileName;		// 파일명
	private String fileOriginName;		// 원본파일명
	private String filePath;		// 파일 경로
	@Transient
	private String fileStatus;		//파일 상태
	@Transient
	private String newFileName;		//새로운파일명
}
