package com.ezen.makingbaking.service.user;

import java.util.Map;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;

public interface UserService {
	void join(User user);

	User idcheck(User user);

	User findid(UserDTO userDTO);

	UserDTO searchPwd(String userId, String userName);

	Map<String, Object> findLoginPasswd(Map<String, Object> param);

//	MailDTO createMailAndChangePassword(String userMail);
//
//	void updatePassword(String str, String userMail);
//
//	String getTempPassword();
//
//	void mailSend(MailDTO mailDTO);
//
//	void updatePassWord(String userId, String userPw);
}
