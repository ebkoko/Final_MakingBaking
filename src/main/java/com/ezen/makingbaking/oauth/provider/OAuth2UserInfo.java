package com.ezen.makingbaking.oauth.provider;

public interface OAuth2UserInfo {
	String getProviderId();
	String getProvider();
	String getBirth();
	String getEmail();
	String getName();
	String getGender();
}
