package com.ezen.makingbaking.service.user;

import com.ezen.makingbaking.entity.User;

public interface UserService {
	void join(User user);

	User idcheck(User user);
	
}
