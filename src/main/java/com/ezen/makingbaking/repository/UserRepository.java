package com.ezen.makingbaking.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;

@Transactional
//딜리트나 업데이트 시에는 데이터가 바뀌는 작업이여서 entity랑 table을 동기화 해주는 @Modifying, @Transactional이 선언되어야 한다
public interface UserRepository extends JpaRepository<User, String> {
	User findByUserIdAndUserPw(
			@Param("userId") String userId, 
			@Param("userPw") String userPw);
	
	//아이디찾기
	User findByUserNameAndUserTel(@Param("userName") String userName, @Param("userTel") String userTel);
	
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
	
	@Modifying
	@Query (value="DELETE FROM T_MB_USER"
			+ "    WHERE USER_ID = :userId",
			nativeQuery=true)
	void quitUser(@Param("userId") String userId);

	@Modifying
	@Query(value="UPDATE T_MB_USER"
			+ " SET USER_ID = :#{#userId.userId},"
			+ "		USER_PW = :#{#userId.userPw},"
			+ "		USER_NAME = :#{#userId.userName},"
			+ "		USER_BIRTH = :#{#userId.userBirth},"
			+ "		USER_GENDER = :#{#userId.userGender},"
			+ "		USER_TEL = :#{#userId.userTel},"
			+ "		USER_MAIL = :#{#userId.userMail},"
			+ "		USER_ADDR1 = :#{#userId.userAddr1},"
			+ "		USER_ADDR2 = :#{#userId.userAddr2},"
			+ "		USER_ADDR3 = :#{#userId.userAddr3}"
			+ " WHERE USER_ID = :#{#userId.userId}",
			nativeQuery=true)
	int updateUser(@Param("userId") User userId);
	
	//관리자 회원 검색_선민
	Page<User> findAllByOrderByUserName(Pageable pageable);
	Page<User> findByUserNameContainingOrderByUserName(String searchKeyword, Pageable pageable); //이름
	Page<User> findByUserIdContainingOrderByUserName(String searchKeyword, Pageable pageable); //아이디
	Page<User> findByUserNameContainingOrUserIdOrderByUserName(String searchKeyword1, String searchKeyword2, Pageable pageable);
	
	//회원리스트 회원상세정보-팝업_선민
	@Query(value="SELECT * FROM T_MB_USER WHERE USER_ID = :userId", nativeQuery=true)
	User getUserInfoCheck(@Param("userId") String userId);
	
}
