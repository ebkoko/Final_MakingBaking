package com.ezen.makingbaking.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.repository.UserRepository;
import com.ezen.makingbaking.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void join(User user) {
		userRepository.save(user);
	}
	
	@Override
	public User idcheck(User user) {
		if(!userRepository.findById(user.getUserId()).isEmpty()) {
			return userRepository.findById(user.getUserId()).get();
		} else {
			return null;
		}
	}
}
