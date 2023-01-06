package com.ezen.makingbaking.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.entity.User;

@Transactional
public interface UserRepository extends JpaRepository<User, String> {
	User findByUserIdAndUserPw(
			@Param("userId") String userId, 
			@Param("userPw") String userPw);
	
	@Modifying
	@Query(value="UPDATE T_MB_USER"
			+ "	SET USER_PW = :userPw"
			+ " WHERE USER_ID = :userId",
			nativeQuery=true)
	void updatePw(@Param("userId") String userId, @Param("userPw") String userPw);

	@Query (value="SELECT *\r\n"
			+ "		FROM T_MB_USER\r\n"
			+ "		WHERE \r\n"
			+ "		USER_ID = :userId\r\n"
			+ "		AND\r\n"
			+ "		USER_NAME = :userName",
			nativeQuery=true) //jpa에서 개발자 마음대로 함수명을 정하고 쿼리도 추가하려면 nativeQuery=true를 필수적으로 추가해야한다.
	User searchPw(@Param("userId") String userId, @Param("userName") String userName);
}
