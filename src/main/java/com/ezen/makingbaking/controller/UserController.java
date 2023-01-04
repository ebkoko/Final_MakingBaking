package com.ezen.makingbaking.controller;

import java.io.IOException;
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
	
}