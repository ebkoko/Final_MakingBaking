package com.ezen.makingbaking.service.kakaopay;

import com.ezen.makingbaking.dto.CancelResponseDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface KakaoPayOrderCancelService {
	CancelResponseDTO cancelReady(OrderDTO orderDTO) throws JsonMappingException, JsonProcessingException;
}
