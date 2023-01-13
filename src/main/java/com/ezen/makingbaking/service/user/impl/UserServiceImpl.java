package com.ezen.makingbaking.service.user.impl;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.repository.UserRepository;
import com.ezen.makingbaking.service.mail.MailService;
import com.ezen.makingbaking.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
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
	
	@Override
	   public Map<String, Object> findLoginPasswd(Map<String, Object> param) {
	      String userId = (String) param.get("userId");
	      String userName = (String) param.get("userName");
	      String userMail = (String) param.get("userMail");
	      User user  = userRepository.searchPw(userId, userName);

	      if (user == null) {
	         return Maps.of("resultCode", "F-1", "msg", "일치하는 회원이 없습니다.");
	      }
	      
	      char[] charSet = new char[] 
	            { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
	             'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 
	            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 
	            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 
	            's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', 
	            '$', '%', '^', '&' }; 
	      
	      StringBuffer sb = new StringBuffer();
	      SecureRandom sr = new SecureRandom(); 
	      sr.setSeed(new Date().getTime()); 
	      int idx = 0; int len = charSet.length; 
	      for (int i=0; i<10; i++) { 
	         idx = sr.nextInt(len);
	         sb.append(charSet[idx]); 
	      }

	      String tempLoginPasswd = sb.toString();
	            
	      user.setUserPw(tempLoginPasswd);
	      
	      String mailTitle = userName + "님, 당신의 계정(" + userId + ")의 임시 패스워드 입니다.";
	      String mailBody = "임시 패스워드 : " + tempLoginPasswd;
	      mailService.send(userMail, mailTitle, mailBody);


	      // 비밀번호 암호화해주는 메서드
	      tempLoginPasswd = passwordEncoder.encode(tempLoginPasswd);
	      //tempLoginPasswd = PawMap1124Application.encodePwd(tempLoginPasswd);
	      // 데이터 베이스 값은 암호한 값으로 저장시킨다.
	      userRepository.updatePw(userId, tempLoginPasswd);
	      

	      return Maps.of("resultCode", "S-1", "msg", "입력하신 메일로 임시 패스워드가 발송되었습니다.");
	      
	   }

	@Override
	public User searchPw(String userId, String userName) {
		return userRepository.searchPw(userId, userName);
	}

	@Override
	public User findid(UserDTO userDTO) {
		System.out.println(userDTO.toString());
		return userRepository.findByUserNameAndUserTel(userDTO.getUserName(), userDTO.getUserTel());
	}

	@Override
	public void quitUser(String userId) {
		userRepository.quitUser(userId);
	}

	@Override
	public void pwUser(String userId) {
		return;
	}

	@Override
	public void updateUser(User user) {
		userRepository.updateUser(user);
	}
}
