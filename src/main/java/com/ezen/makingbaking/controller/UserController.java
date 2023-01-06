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
import com.ezen.makingbaking.service.mypage.MypageService;
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
	
    @PostMapping(value="/findPW", produces = "text/html; charset=UTF-8;")
	public ModelAndView userchPw(@RequestParam Map<String, Object> param , HttpServletResponse response) throws IOException {
       
       String userId = (String) param.get("userId");
       String userName = (String) param.get("userName");
       String userMail = (String) param.get("userMail");
       
       User user  = userService.searchPw(userId, userName);
       
       response.setCharacterEncoding("UTF-8");
   	   response.setContentType("text/html; charset=UTF-8");
       PrintWriter out = response.getWriter();
        if(user == null) {
        	
          
          // 입력한 정보가 일치하지 않을 때
          out.println("<script>alert('일치하는 회원이 없습니다.'); location.href='/user/findPW';</script>");
          
          out.flush();
        }else if (!user.getUserMail().equals(userMail)){
             
             out.println("<script>alert('이메일 정보가 일치하지 않습니다.'); location.href='/user/findPW';</script>");
             
             out.flush();
       } else {
          
          // 입력한 정보와 회원정보가 일치할 때 
          out.println("<script>alert('입력하신 메일로 임시 패스워드가 발송되었습니다. 마이페이지에서 비밀번호를 변경해주세요.'); location.href='/home/main';</script>");
          
          out.flush();
          Map<String, Object> findLoginIdRs = userService.findLoginPasswd(param);
       
       }
        
        ModelAndView mv = new ModelAndView();
		mv.setViewName("/home/main");
		return mv;
       
    }
    
    //회원탈퇴
    @RequestMapping("/quitUser") //ajax = ResponseEntity, submit = ModelAndView
	public ModelAndView quitUser(@RequestParam("userPw") String userPw, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
		User user = User.builder()
						.userId(customUserDetails.getUsername())
						.build();
    	
    	
    	User dbUser = userService.idcheck(user);
    	
    	if(passwordEncoder.matches(userPw, dbUser.getUserPw())) {
    		
    	} else {
    		
    		ModelAndView mv = new ModelAndView();
    		mv.setViewName("/mypage/quit");
    		return mv;
    	}
    	
    	userService.quitUser(userPw);
		
    	ModelAndView mv = new ModelAndView();
		mv.setViewName("/home/main");
		return mv;
	}
}