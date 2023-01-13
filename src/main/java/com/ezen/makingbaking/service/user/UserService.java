package com.ezen.makingbaking.service.user;

import java.util.Map;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;

public interface UserService {
	void join(User user);

	User idcheck(User user);

	User findid(UserDTO userDTO);

	User searchPw(String userId, String userName);

	Map<String, Object> findLoginPasswd(Map<String, Object> param);

	void quitUser(String userId);

	void pwUser(String userId);

	void updateUser(User user);
}
