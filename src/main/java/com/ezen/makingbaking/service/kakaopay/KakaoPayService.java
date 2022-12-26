package com.ezen.makingbaking.service.kakaopay;

import com.ezen.makingbaking.dto.ReadyResponseDTO;

public interface KakaoPayService {
	ReadyResponseDTO payReady(int totalAmount);
}
