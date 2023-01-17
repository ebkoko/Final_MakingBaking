package com.ezen.makingbaking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ezen.makingbaking.configuration.handler.LoginFailureHandler;
import com.ezen.makingbaking.configuration.handler.LoginSuccessHandler;
import com.ezen.makingbaking.oauth.Oauth2UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
		@Autowired
		private LoginFailureHandler loginFailureHandler;
		
		@Autowired
		private LoginSuccessHandler loginSuccessHandler;
		
		@Autowired
		private Oauth2UserService oauth2UserService;
		
		@Bean
		public static PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
		
		@Bean
		public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
			http.authorizeRequests().antMatchers("/").permitAll()
									.antMatchers("/css/**").permitAll()
									.antMatchers("/js/**").permitAll()
									.antMatchers("/images/**").permitAll()
									.antMatchers("/upload/**").permitAll()
									.antMatchers("/home/**").permitAll()
									.antMatchers("/main/join").permitAll()
									.antMatchers("/main/login").permitAll()
									.antMatchers("/user/idcheck").permitAll()
									.antMatchers("/home/main").permitAll()
									.antMatchers("/main/main").permitAll()
									.antMatchers("/user/loginProc").permitAll()
									.antMatchers("/user/join").permitAll()
									.antMatchers("/user/idcheck").permitAll()
									.antMatchers("/user/findID").permitAll()
									.antMatchers("/user/findPW").permitAll()
									.antMatchers("/dayclass/onedayClass").permitAll()
									.antMatchers("/dayclass/**").permitAll()
									.antMatchers("/item/list").permitAll()
									.antMatchers("/item/item").permitAll()
									.antMatchers("/item/**").permitAll()
									.antMatchers("/board/qnaList/**").permitAll()
									.antMatchers("/board/updateQnaCnt/**").permitAll()
									.antMatchers("/board/qna/**").permitAll()
									.antMatchers("/board/noticeList/**").permitAll()
									.antMatchers("/board/updateNoticeCnt/**").permitAll()
									.antMatchers("/board/notice/**").permitAll()
									.antMatchers("/board/eventList/**").permitAll()
									.antMatchers("/board/updateEventCnt/**").permitAll()
									.antMatchers("/board/event/**").permitAll()
									.antMatchers("/board/**").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
									.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
									.anyRequest().authenticated();  /* 인증받은 사용자만 접근 가능하게 나중에 .anyRequest().authenticated();로 바꿔야함 */
			
			//로그인
			http.formLogin()
			.loginPage("/home/main?loginPage=Y")
			.usernameParameter("userId")
			.passwordParameter("userPw")
			.loginProcessingUrl("/user/loginProc")
			.successHandler(loginSuccessHandler)
			.failureHandler(loginFailureHandler)
			
			//카카오 로그인
			.and()
			.oauth2Login()
			.loginPage("/home/main?loginPage=Y")
			.defaultSuccessUrl("/user/join")
			.userInfoEndpoint()
			.userService(oauth2UserService);
			
			
			//로그아웃
		http.logout()
			.logoutUrl("/main/logout")
			.invalidateHttpSession(true)
			.logoutSuccessUrl("/main/main");
		
			http.csrf().disable();
			
			return http.build();
		}
}
