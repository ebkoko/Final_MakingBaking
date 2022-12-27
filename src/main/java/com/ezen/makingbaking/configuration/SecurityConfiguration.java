package com.ezen.makingbaking.configuration;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.CollectionUtils;

import com.ezen.makingbaking.configuration.handler.LoginFailureHandler;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
		@Autowired
		private LoginFailureHandler loginFailureHandler;
		
		
		
		@Bean
		public static PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			//권한에 따른 요청주소 매핑
			http.authorizeRequests().antMatchers("/css/**").permitAll()
									.antMatchers("/js/**").permitAll()
									.antMatchers("/images/**").permitAll()
									.antMatchers("/upload/**").permitAll()
									.antMatchers("/main/join").permitAll()
									.antMatchers("/main/login").permitAll()
									.antMatchers("/user/idcheck").permitAll()
									.antMatchers("/user/loginProc").permitAll()
									.anyRequest().permitAll();
			
			//로그인, 로그아웃 설정
			http.formLogin()
			.loginPage("/home/main?loginPage=Y")
			.usernameParameter("userId")
			.passwordParameter("userPw")
			.loginProcessingUrl("/user/loginProc")
			.defaultSuccessUrl("/main/main")
			.failureHandler(loginFailureHandler);
			
			
			
			
			//카카오 로그인
			//OAuth기반 로그인 처리
			//.and()
			//.oauth2Login()
			//.loginPage("/main/login")
			//토큰 발행 후 처리
			//토큰이 발행되면 사용자 정보를 받아서 처리 가능해지는 데
			//사용자 정보를 웹 사이트에 맞도록 변경해주는 작업 필요
			//.userInfoEndpoint() //사용자 정보를 다 가지고 왔을 때
			//사용자 정보를 웹 사이트에 맞도록 변경해주는 service 클래스 등록
			//.userService(oauth2UserService);
		http.logout()
			.logoutUrl("/main/logout")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/main/main");
			
			
		
		
		
		
		
			http.csrf().disable();
			
			return http.build();
		}
}
