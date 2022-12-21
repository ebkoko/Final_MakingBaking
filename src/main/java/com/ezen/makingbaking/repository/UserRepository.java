package com.ezen.makingbaking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	User findByUserIdAndUserPw(
			@Param("userId") String userId, 
			@Param("userPw") String userPw);
}
