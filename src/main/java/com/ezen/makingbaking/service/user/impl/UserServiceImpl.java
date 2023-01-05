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

//	@Override
//	public User findid(UserDTO userDTO) {
//		System.out.println(userDTO.toString());
//		return userRepository.findByUserNameAndUserTel(userDTO.getUserNm(), userDTO.getUserTel());
//	}
//	
//	// 메일 내용을 생성하고 임시 비밀번호로 회원 비밀번호를 변경 
//    @Override
//    public MailDTO createMailAndChangePassword(String userMail) {
//        String str = getTempPassword();
//        MailDTO dto = new MailDTO();
//        dto.setAddress(userMail);
//        dto.setTitle("임시비밀번호 안내 이메일 입니다.");
//        dto.setMessage("임시비밀번호 안내 관련 이메일 입니다." + " 회원님의 임시 비밀번호는 "
//                + str + " 입니다." + "로그인 후에 비밀번호를 변경을 해주세요");
//        updatePassword(str,userMail);
//        return dto;
//    }
//
//    //임시 비밀번호로 업데이트
//    @Override
//    public void updatePassword(String str, String userMail){
//        String userPw = passwordEncoder.encode(str);
//        String userId = userRepository.findByUserMail(userMail).getUserId();
//        userRepository.updatePassword(userId, userPw);
//    }
//
//    //랜덤함수로 임시비밀번호 구문 만들기
//    @Override
//    public String getTempPassword(){
//        char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
//                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
//
//        String str = "";
//
//        // 문자 배열 길이의 값을 랜덤으로 10개를 뽑아 구문을 작성함
//        int idx = 0;
//        for (int i = 0; i < 10; i++) {
//            idx = (int) (charSet.length * Math.random());
//            str += charSet[idx];
//        }
//        return str;
//    }
//    // 메일보내기
//    @Override
//    public void mailSend(MailDTO mailDTO) {
//        System.out.println("전송완료");
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(mailDTO.getAddress());
//        message.setSubject(mailDTO.getTitle());
//        message.setText(mailDTO.getMessage());
//        message.setFrom("fownd789@naver.com");
//        message.setReplyTo("fownd789@naver.com");
//        System.out.println("message"+message);
//        mailSender.send(message);
//    }
//
//    //비밀번호 변경
//    @Override
//    public void updatePassWord(String userId, String userPw) {
//    	String encodedUserPw = passwordEncoder.encode(userPw);
//    	userRepository.updatePassword(userId, encodedUserPw);
//    }
	@Override
	   public Map<String, Object> findLoginPasswd(Map<String, Object> param) {
	      String userId = (String) param.get("userId");
	      String userName = (String) param.get("userName");
	      String userMail = (String) param.get("userMail");
	      UserDTO userDTO  = userRepository.searchPwd(userId, userName);

	      if (userDTO == null) {
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
	            
	      userDTO.setUserPw(tempLoginPasswd);
	      
	      String mailTitle = userName + "님, 당신의 계정(" + userId + ")의 임시 패스워드 입니다.";
	      String mailBody = "임시 패스워드 : " + tempLoginPasswd;
	      mailService.send(userMail, mailTitle, mailBody);


	      // 비밀번호 암호화해주는 메서드
	      tempLoginPasswd = passwordEncoder.encode(tempLoginPasswd);
	      //tempLoginPasswd = PawMap1124Application.encodePwd(tempLoginPasswd);
	      // 데이터 베이스 값은 암호한 값으로 저장시킨다.
	      UserDTO DTO = new UserDTO();
	      DTO.setUserId(userId);
	      DTO.setUserPw(tempLoginPasswd);
	      userRepository.update(DTO);
	      

	      return Maps.of("resultCode", "S-1", "msg", "입력하신 메일로 임시 패스워드가 발송되었습니다.");
	      
	   }

	@Override
	public UserDTO searchPwd(String userId, String userName) {
		return null;
	}

	@Override
	public User findid(UserDTO userDTO) {
		return null;
	}
}
