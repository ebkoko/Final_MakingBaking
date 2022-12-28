package com.ezen.makingbaking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDTO {
	private int itemNo;		// 상품번호
	private String itemName;	// 상품이름
	private String itemMinName; // 상품소제목
	private int itemPrice;		// 상품가격
	private String itemDetails;		// 상품상세내용
	private int itemStock;		// 재고량
	private char itemStatus;	// 판매상태(기본값 Y, 재고없음 S, 판매중지 N)
	private String itemCate;	// 카테고리(쿠키, 케잌..)
	private String itemRegdate;	// 상품추가일
	private String itemExpDate;	// 유통기한
	private String itemOrigin;	// 원산지
	private String itemAllergyInfo;	// 알레르기정보
	private String searchCondition;
	private String searchKeyword;
}


//ITME_STATUS LIKE '%#{searchKeyword}%'