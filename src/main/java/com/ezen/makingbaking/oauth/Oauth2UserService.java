package com.ezen.makingbaking.oauth;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.oauth.provider.KakaoUserInfo;
import com.ezen.makingbaking.oauth.provider.OAuth2UserInfo;
import com.ezen.makingbaking.repository.UserRepository;


@Service
public class Oauth2UserService extends DefaultOAuth2UserService{
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) 
			throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		Map<String, Object> temp = oAuth2User.getAttributes();
		
		Iterator<String> iter = temp.keySet().iterator();
		
		while(iter.hasNext()) {
			System.out.println(iter.next());
			System.out.println(userRequest.getAccessToken().getTokenValue());
		}
		
		String userName= "";
		String providerId = "";
		
		OAuth2UserInfo oAuth2UserInfo = null;
		
		if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
			providerId = oAuth2UserInfo.getProviderId();
			userName = oAuth2UserInfo.getName();
		} else {
			System.out.println("카카오 로그인만 지원합니다.");
		}
		
		String provider = oAuth2UserInfo.getProvider();
		String userId = oAuth2UserInfo.getEmail();
		String password = passwordEncoder.encode(oAuth2UserInfo.getName());
		String birth = oAuth2UserInfo.getBirth();
		String email = oAuth2UserInfo.getEmail();
		String role = "ROLE_USER";
		
		User user;
		
		if(userRepository.findById(userId).isPresent()) {
			user = userRepository.findById(userId).get();
			user.setJoinYn("Y");
		} else {
			user = null;
		}
		
		//회원가입 처리
		if(user == null) {
			user = User.builder()
					   .userId(userId)
					   .userPw(password)
					   .userName(userName)
					   .userBirth(birth)
					   .userMail(email)
					   .userRole(role)
					   .joinYn("N")
					   .userGender((char) 4)
					   .build();
			
		}
		
		
		return CustomUserDetails.builder()
								.user(user)
								.attributes(oAuth2User.getAttributes())
								.build();
	}
	
}