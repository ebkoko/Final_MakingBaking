package com.ezen.makingbaking.configuration.handler;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler{
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, 
			AuthenticationException exception) throws IOException, ServletException {
		System.out.println(exception.getMessage());
		
		String errorMessage = getExceptionMessage(exception);
		
		errorMessage = URLEncoder.encode(errorMessage, "UTF-8");
		
		setDefaultFailureUrl("/main/main?error=true&errorMsg=" + errorMessage);
		
		super.onAuthenticationFailure(request, response, exception);
	}
	
	private String getExceptionMessage(AuthenticationException exception) {
		if(exception instanceof BadCredentialsException) {
			return "비밀번호불일치";
		} else if(exception instanceof UsernameNotFoundException) {
			return "계정없음";
		} else if(exception instanceof AccountExpiredException) {
			return "계정만료";
		} else if(exception instanceof CredentialsExpiredException) {
			return "비밀번호만료";
		} else if(exception instanceof DisabledException) {
			return "계정비활성화";
		} else if(exception instanceof LockedException) {
			return "계정잠김";
		} else if(exception.getMessage().contains("UserDetailsService returned null")) {
			return "계정없음";
		} else {
			return "확인되지 않은 에러 발생";
		}
	}
}
