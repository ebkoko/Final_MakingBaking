package com.ezen.makingbaking.service.user;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;

public interface UserService {
	void join(User user);

	User idcheck(User user);

	User findid(UserDTO userDTO);

	/*
	public boolean userCheck(String userId, String userMail, String userTel) {
		
		User user = userRepository.findUserByUserId(userEmail);
		if(user!=null && user.getUserName().equals(userName)) {
            return true;
        }
        else {
            return false;
        }
	};

	void mailSend(UserDTO userDTO);

	UserDTO createMailAndChangePassword(String userId, String userMail, String userTel);
	*/
}
