package com.ezen.makingbaking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
// security의 filterchain을 구현하기 위한 어노테이션
@EnableWebSecurity
public class SecurityConfiguration {
	//비밀번호 암호화를 위한 PasswordEncoder
		//security에 의해 로그인 처리될 때 비밀번호 비교 시 무조건 사용
		//복호화는 불가능, match(사용자입력값(그냥 String), DB에 저장된 암호화된 비밀번호) => true나 false로 리턴
		@Bean
		public static PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		//필터 체인 구현(HttpSecurity 객체 사용)
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			//권한에 따른 요청주소 매핑
			http.authorizeRequests().antMatchers("/css/**").permitAll()
									.antMatchers("/js/**").permitAll()
									.antMatchers("/images/**").permitAll()
									.antMatchers("/upload/**").permitAll()
									.anyRequest().permitAll();
			
			http.csrf().disable();
			
			return http.build();
		}
}
