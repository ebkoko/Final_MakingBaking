package com.ezen.makingbaking.service.kakaopay;

import com.ezen.makingbaking.dto.CancelResponseDTO;
import com.ezen.makingbaking.dto.ReserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface KakaoPayReserCancelService {
	CancelResponseDTO cancelReserReady(ReserDTO reserDTO) throws JsonMappingException, JsonProcessingException;
}
