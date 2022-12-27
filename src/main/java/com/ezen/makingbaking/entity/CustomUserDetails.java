package com.ezen.makingbaking.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails{
	private User user;
	
	//소셜로그인에서 사용자 정보를 담아줄 맵
	Map<String, Object> attributes;
	
	//권한정보 제공
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
		
		auths.add(
			new GrantedAuthority() {
				@Override
				public String getAuthority() {
					return user.getUserId();
				}
			}
		);
		
		return auths;
	}
	
	//비밀번호 제공
	@Override
	public String getPassword() {
		return user.getUserPw();
	}
	
	//아이디 제공
	@Override
	public String getUsername() {
		return user.getUserId();
	}
	
	//계정 만료 여부
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	//계정 잠김 여부
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	//계정 인증정보를 항상 저장할 지 여부
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	//계정 활성화 여부
	@Override
	public boolean isEnabled() {
		return true;
	}
	/*
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getName() {
		return null;
	}
	*/
}
