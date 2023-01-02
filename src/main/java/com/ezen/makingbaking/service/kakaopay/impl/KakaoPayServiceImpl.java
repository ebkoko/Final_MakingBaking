package com.ezen.makingbaking.service.kakaopay.impl;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ezen.makingbaking.common.CamelHashMap;
import com.ezen.makingbaking.dto.ApproveResponseDTO;
import com.ezen.makingbaking.dto.CartDTO;
import com.ezen.makingbaking.dto.ReadyResponseDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.repository.CartRepository;
import com.ezen.makingbaking.repository.OrderRepository;
import com.ezen.makingbaking.service.kakaopay.KakaoPayService;

@Service
public class KakaoPayServiceImpl implements KakaoPayService {
	@Autowired
	private CartRepository cartRepository;
	
	public ReadyResponseDTO payReady(int totalAmount, @AuthenticationPrincipal CustomUserDetails customUser) {
		//User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
		List<CamelHashMap> carts = cartRepository.findAllItemInfoinCart(customUser.getUsername());
		
		String[] cartNames = new String[carts.size()];
//		for(CamelHashMap cart: carts) {
//			for(int i=0; i< carts.size(); i++) {
//				cartNames[i] = cart.getClassTitle(); // itemName을 가져와야함..
//			}
//		}	
		String itemName = cartNames[0] + " 그외" + (carts.size()-1);
		System.out.println("상품명:"+itemName);
		
		// 주문번호 생성
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String ym = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String ymd = ym + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		String subNum = "";
		
		for(int i = 1; i <= 6; i++) {
			subNum += (int)(Math.random() * 10);
		}
		
		String orderNo = ymd + subNum;
		
        // 카카오가 요구한 결제요청 request값을 담아줍니다. 
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME"); // 가맹점 코드
		parameters.add("partner_order_id", orderNo); // 주문번호
		parameters.add("partner_user_id", "MakingBaking"); // 사이트명
		parameters.add("item_name", itemName); // 상품명
		parameters.add("quantity", String.valueOf(carts.size())); // 상품 수량
		parameters.add("total_amount", String.valueOf(totalAmount)); // 상품 총액
		parameters.add("tax_free_amount", "0"); // 상품 비과세 금액
		parameters.add("approval_url", "http:localhost:9900/order/orderComplete"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http:localhost:9900/order/order"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http:localhost:9900/cart/cartList"); // 결제 실패시 넘어갈 url
		
		System.out.println("주문번호:"+ parameters.get("partner_order_id")) ;
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		// 외부url요청 통로 열기.
		RestTemplate template = new RestTemplate();
		String url = "https:kapi.kakao.com/v1/payment/ready";
        // template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		ReadyResponseDTO readyResponse = template.postForObject(url, requestEntity, ReadyResponseDTO.class);
		System.out.println("결제준비 응답객체: " + readyResponse);
        // 받아온 값 return
		return readyResponse;
	}
	
    // 결제 승인요청 메서드
	public ApproveResponseDTO payApprove(String tid, String pgToken, @AuthenticationPrincipal CustomUserDetails customUser) {
		//User user =  (User)SessionUtils.getAttribute("LOGIN_USER");
		List<CamelHashMap> carts = cartRepository.findAllItemInfoinCart(customUser.getUsername());
		
		// 주문번호 생성
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String ym = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String ymd = ym + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		String subNum = "";
		
		for(int i = 1; i <= 6; i++) {
			subNum += (int)(Math.random() * 10);
		}
		
		String orderNo = ymd + subNum;
		
		// request값 담기.
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("tid", tid);
		parameters.add("partner_order_id", orderNo);
		parameters.add("partner_user_id", "MakingBaking");
		parameters.add("pg_token", pgToken);
		
        // 하나의 map안에 header와 parameter값을 담아줌.
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
		
        // 외부url 통신
		RestTemplate template = new RestTemplate();
		String url = "https:kapi.kakao.com/v1/payment/approve";
        // 보낼 외부 url, 요청 메시지(header,parameter), 처리후 값을 받아올 클래스. 
		ApproveResponseDTO approveResponse = template.postForObject(url, requestEntity, ApproveResponseDTO.class);
		System.out.println("결제승인 응답객체: " + approveResponse);
		
		return approveResponse;
	}
	 //header() 셋팅
	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "8533975305e5659ab30d1f5b9da813c2");
		headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		return headers;
	}

	@Override
	public ReadyResponseDTO payReady(int totalAmount) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
