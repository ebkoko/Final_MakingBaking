package com.ezen.makingbaking.repository;

import javax.transaction.Transactional;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	User findByUserIdAndUserPw(
			@Param("userId") String userId, 
			@Param("userPw") String userPw);
	
//	User findByUserNameAndUserTel(@Param("userName") String userName, @Param("userTel") String userTel);
//	
//	User findByUserMail(String userMail);
//	
//	@Modifying
//	@Query(value="UPDATE T_MB_USER"
//			+ "	SET USER_PW = :userPw"
//			+ " WHERE USER_ID = :userId",
//			nativeQuery=true)
//	void updatePassword(@Param("userId") String userId, @Param("userPw") String userPw);

}
