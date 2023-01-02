package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImgFileDTO {
	private int fileReferNo;		// 참조항목번호(상품번호, 클래스번호, 리뷰글번호) ex)상품1
	private int fileNo;				// 첨부파일번호 ex) (상품1의) 이미지1,  이미지2, 이미지3, ...
	private String fileType;		// 타입(item, class, review)
	private String fileName;		// 파일명
	private String fileOriginName;		// 원본파일명
	private String filePath;		// 파일 경로
	private String fileStatus;		//파일상태
	private String newFileName;		//새로운파일명
}
