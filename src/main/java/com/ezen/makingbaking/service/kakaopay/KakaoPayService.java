package com.ezen.makingbaking.service.kakaopay;

import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface KakaoPayService {
	ReadyResponseDTO payReady(int totalAmount, String itemList) throws JsonMappingException, JsonProcessingException;
	
	ApproveResponseDTO payApprove(String tid, String pgToken, long orderNo);

}
