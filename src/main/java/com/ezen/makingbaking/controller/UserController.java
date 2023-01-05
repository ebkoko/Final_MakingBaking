package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/join")
	public ModelAndView joinView(HttpSession session, 
			@AuthenticationPrincipal CustomUserDetails customUser,
			HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		User user = null;
		
		if(customUser != null)
			user = customUser.getUser();
		
		if(user == null || user.getJoinYn().equals("N")) {
			SecurityContext securityContext = SecurityContextHolder.getContext();
			Authentication authentication = null;
			securityContext.setAuthentication(authentication);
			session.setAttribute("SPRING_SERCURITY_CONTEXT", securityContext);
			
			mv.setViewName("user/join.html");
			mv.addObject("socialUser", user);
		} else {
			response.sendRedirect("/home/main");
		}
		return mv;
	}

	@PostMapping("/join")
	public ResponseEntity<?> join(UserDTO userDTO) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			
			User user = User.builder()
							.userId(userDTO.getUserId())
							.userPw(passwordEncoder.encode(userDTO.getUserPw()))
							.userName(userDTO.getUserNm())
							.userNo(userDTO.getUserNo())
							.userBirth(userDTO.getUserBirth())
							.userGender(userDTO.getUserGender())
							.userTel(userDTO.getUserTel())
							.userMail(userDTO.getUserMail())
							.userAddr1(userDTO.getUserAddr1())
							.userAddr2(userDTO.getUserAddr2())
							.userAddr3(userDTO.getUserAddr3())
							.build();
			
			userService.join(user);
			
			returnMap.put("joinMsg", "joinSuccess");
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			returnMap.put("joinMsg", "joinFail");
			responseDTO.setErrorMessage(e.getMessage());
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@PostMapping("/idcheck")
	public ResponseEntity<?> idCheck(UserDTO userDTO) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		
			try {
			User user = User.builder()
							.userId(userDTO.getUserId())
							.build();
			
			User checkedUser = userService.idcheck(user);
			
			if(checkedUser != null) {
				returnMap.put("msg", "duplicatedId");
			} else {
				returnMap.put("msg", "idOk");
			}
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	@GetMapping("/findID")
	public ModelAndView findIdView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/findID.html");
		return mv;
	}
	
	@PostMapping("/findID")
	public String findID(UserDTO userDTO) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> resultMap = new HashMap<String, String>();
			
			User findId = userService.findid(userDTO);
			
			if(findId != null) {
				resultMap.put("msg", "ok");
				resultMap.put("findID", findId.getUserId());
			} else {
				resultMap.put("msg", "fail");
			}
			
			String jsonStr = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(resultMap);
			
			return jsonStr;
	}
	
	@GetMapping("/findPW")
	public ModelAndView findPwView() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("user/findPW.html");
		return mv;
	}

//	@PostMapping("/sendEmail")
//	public ModelAndView sendMail(MailDTO mailDTO, @RequestParam("userMail") String userMail, 
//			@RequestParam("userId") String userId){
//		System.out.println("fdasf");
//		MailDTO sendMailDTO = userService.createMailAndChangePassword(userMail);
//		userService.mailSend(sendMailDTO);
//		ModelAndView mv = new ModelAndView();
//		mv.setViewName("/main/main");
//		return mv;
//	}
	
	
	//passwordEncoder.match("사용자 입력 비밀번호", "암호화된 비밀번호")
	/*
	@GetMapping("/findPw")
    public @ResponseBody Map<String, Boolean> findPW(String userId, String userMail, String userTel){
        Map<String,Boolean> json = new HashMap<>();
        boolean pwCheck = userService.userCheck(userId, userMail, userTel);

        System.out.println(pwCheck);
        json.put("check", pwCheck);
        return json;
    }
	
	@PostMapping("/sendMail")
    public @ResponseBody void sendEmail(String userId, String userMail, String userTel){
        UserDTO userDTO = userService.createMailAndChangePassword(userId, userMail, userTel);
        userService.mailSend(userDTO);
    }
	*/
    @PostMapping("/userchPw")
	public String userchPw(@RequestParam Map<String, Object> param , HttpServletResponse response) throws IOException {
       
       String userId = (String) param.get("userId");
       String userName = (String) param.get("userName");
       String userMail = (String) param.get("userMail");
       
       UserDTO user  = userService.searchPwd(userId, userName);
       
       PrintWriter out = response.getWriter();
        if(user == null) {
          response.setContentType("text/html; charset=UTF-8");
          
          // 입력한 정보가 일치하지 않을 때
          out.println("<script>alert('일치하는 회원이 없습니다'); location.href='searchPw';</script>");
          
          out.flush();
        }else if (!user.getUserMail().equals(userMail)){
             response.setContentType("text/html; charset=UTF-8");
             
             out.println("<script>alert('이메일 정보가 일치하지 않습니다'); location.href='searchPw';</script>");
             
             out.flush();
       } else {
          response.setContentType("text/html; charset=UTF-8");
          
          // 입력한 정보와 회원정보가 일치할 때 
          out.println("<script>alert('입력하신 메일로 임시 패스워드가 발송되었습니다'); location.href='searchPw';</script>");
          
          out.flush();
          Map<String, Object> findLoginIdRs = userService.findLoginPasswd(param);
       
       }
        
       return "main";
       
    }
}