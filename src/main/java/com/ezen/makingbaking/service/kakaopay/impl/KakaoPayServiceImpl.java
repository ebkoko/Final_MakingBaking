package com.ezen.makingbaking.service.kakaopay.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.CancelResponseDTO;
import com.ezen.makingbaking.dto.OrderDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.repository.CartRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.service.kakaopay.KakaoPayOrderCancelService;
import com.ezen.makingbaking.service.kakaopay.KakaoPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("kakaoOrder")
public class KakaoPayServiceImpl implements KakaoPayService, KakaoPayOrderCancelService {
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Override
	public ReadyResponseDTO payReady(int totalAmount, String itemList) throws JsonMappingException, JsonProcessingException {
		System.out.println(totalAmount);
		
		List<Map<String, Object>> itemMapList = new ObjectMapper().readValue(itemList, new TypeReference<List<Map<String, Object>>>() {});
		
		System.out.println(itemMapList.get(0).toString());
		
		String[] cartNames = new String[itemMapList.size()];
		
		for(int i = 0; i < itemMapList.size(); i++) {
			cartNames[i] = itemMapList.get(i).get("itemName").toString();
		}
		
		String itemName = cartNames[0] + " 그외" + (itemMapList.size()-1);
		System.out.println("상품명:"+itemName);
		
		long orderNo = orderRepository.getNextOrderNo();
		
        // 카카오가 요구한 결제요청 request값을 담아줍니다. 
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME"); // 가맹점 코드
		parameters.add("partner_order_id", String.valueOf(orderNo)); // 주문번호
		parameters.add("partner_user_id", "MakingBaking"); // 사이트명
		parameters.add("item_name", itemName); // 상품명
		parameters.add("quantity", String.valueOf(itemMapList.size())); // 상품 수량
		parameters.add("total_amount", String.valueOf(totalAmount)); // 상품 총액
		parameters.add("tax_free_amount", "0"); // 상품 비과세 금액
		parameters.add("approval_url", "http://localhost:9900/order/kakaoOrderComplete?orderNo=" + orderNo); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http://localhost:9900/order/kakaoOrderCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http://localhost:9900/order/kakaoOrderFail"); // 결제 실패시 넘어갈 url
		
		System.out.println("주문번호:"+ parameters.get("partner_order_id")) ;
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		// 외부url요청 통로 열기.
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		ReadyResponseDTO readyResponse = template.postForObject(url, requestEntity, ReadyResponseDTO.class);
		System.out.println("결제준비 응답객체: " + readyResponse);
		readyResponse.setOrderNo(orderNo);
		
        // 받아온 값 return
		return readyResponse;
	}
	
    // 결제 승인요청 메서드
	@Override
	public ApproveResponseDTO payApprove(String tid, String pgToken, long orderNo) {
				
		// request값 담기.
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("tid", tid);
		parameters.add("partner_order_id", String.valueOf(orderNo));
		parameters.add("partner_user_id", "MakingBaking");
		parameters.add("pg_token", pgToken);
		
        // 하나의 map안에 header와 parameter값을 담아줌.
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
        // 외부url 통신
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
		ApproveResponseDTO approveResponse = template.postForObject(url, requestEntity, ApproveResponseDTO.class);
		System.out.println("결제승인 응답객체: " + approveResponse);
		
		return approveResponse;
	}
	 //header() 셋팅
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK " + "8533975305e5659ab30d1f5b9da813c2");
		
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}

	@Override
	public CancelResponseDTO cancelReady(OrderDTO orderDTO)
			throws JsonMappingException, JsonProcessingException {

		System.out.println(orderDTO.getOrderTotalPayPrice());
		// 카카오가 요구한 결제취소 요청 request값을 담아줍니다. 
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME"); // 가맹점 코드
		parameters.add("tid", orderDTO.getTid()); // tid
		parameters.add("cancel_amount", String.valueOf(orderDTO.getOrderTotalPayPrice())); // 상품 총액
		parameters.add("cancel_tax_free_amount", "0"); // 상품 비과세 금액
		parameters.add("approval_url", "http://localhost:9900/mypage/getOrderDetail/" + orderDTO.getOrderNo()); // 결제취소 승인시 넘어갈 url
		parameters.add("fail_url", "http://localhost:9900/mypage/myPage"); // 결제취소 실패시 넘어갈 url
		
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		// 외부url요청 통로 열기.
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/cancel";
        // template으로 값을 보내고 받아온 CancelResponse값 cancelResponse에 저장.
		CancelResponseDTO cancelResponse = template.postForObject(url, requestEntity, CancelResponseDTO.class);
		System.out.println("결제취소 응답객체: " + cancelResponse);
		
		return cancelResponse;
	}

	
}
