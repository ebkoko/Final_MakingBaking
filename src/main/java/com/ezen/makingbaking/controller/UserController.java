package com.ezen.makingbaking.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.CustomUserDetails;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.user.UserService;
import com.ezen.makingbaking.service.user.impl.UserDetailsServiceImpl;
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
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@GetMapping("/join")
	public ModelAndView joinView(HttpSession session, 
			@AuthenticationPrincipal CustomUserDetails customUser,
			HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView();
		
		User user = null;
		
		if(customUser != null)
			user = customUser.getUser();
		//선생님한테 가기
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
							.userName(userDTO.getUserName())
							.userNo(userDTO.getUserNo())
							.userBirth(userDTO.getUserBirth())
							.userGender(userDTO.getUserGender())
							.userTel(userDTO.getUserTel())
							.userMail(userDTO.getUserMail())
							.userAddr1(userDTO.getUserAddr1())
							.userAddr2(userDTO.getUserAddr2())
							.userAddr3(userDTO.getUserAddr3())
							.userRegdate(LocalDateTime.now())
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
    @PostMapping("/quitUser") //ajax = ResponseEntity, submit = ModelAndView
	public void quitUser(HttpServletResponse response, @RequestParam("userPw") String userPw, 
			@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
		User user = User.builder()
						.userId(customUserDetails.getUsername())
						.build();
    	
    	
    	User dbUser = userService.idcheck(user);
    	
    	if(!passwordEncoder.matches(userPw, dbUser.getUserPw())) {
    		response.sendRedirect("/mypage/quit?quitMsg=N");
    	} else {
    		userService.quitUser(user.getUserId());
    		
    		SecurityContextHolder.clearContext();
    		//1. ModelAndView로 리턴할때에는 이동할 html에 표출할 데이터를 모델에 담아서 리턴한단
    		//2. 만약에 void형태일때 redirect로 그 페이지를 불러오는 요청을 호출한다.
    		//mv.addObject("", mainService.getMainList);
    		
    		response.sendRedirect("/main/main?quitMsg=Y");
    	}
    	
	}
    
    //정보수정시 비번 입력
    @GetMapping("/changeInfoPw")
	public ModelAndView changeInfoPwView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("mypage/changeInfoPw.html");
		if(request.getParameter("quitMsg") != null && !request.getParameter("quitMsg").equals("")) {
	         mv.addObject("quitMsg", request.getParameter("quitMsg").toString());
	      }
		return mv;
	}
    
    @PostMapping("/changeInfoPw")
	public void changeInfoPw(@RequestParam("userPw") String userPw, HttpServletResponse response,
			@AuthenticationPrincipal CustomUserDetails customUserDetails) throws IOException {
		User user = User.builder()
						.userId(customUserDetails.getUsername())
						.build();
    	
    	
    	User dbUser = userService.idcheck(user);
    	
    	if(!passwordEncoder.matches(userPw, dbUser.getUserPw())) {
    		response.sendRedirect("/user/changeInfoPw?quitMsg=N");
    	} else {
    		userService.pwUser(user.getUserId());
    		
    		response.sendRedirect("/mypage/changeInfo");
    	}
    	
	}
    
    //정보수정
    @PostMapping("/changeInfo")
    public ModelAndView changeInfo(UserDTO userDTO, HttpSession session,
    		@AuthenticationPrincipal CustomUserDetails customUserDetails, 
    		HttpServletResponse response, Model model) throws IOException {
    	User user = User.builder()
		    			.userId(userDTO.getUserId())
		    			.userPw(userDTO.getUserPw() == null || userDTO.getUserPw().equals("") ?
		    					customUserDetails.getUser().getUserPw() : passwordEncoder.encode(userDTO.getUserPw()))
						.userName(userDTO.getUserName())
						.userBirth(userDTO.getUserBirth())
						.userGender(userDTO.getUserGender())
						.userTel(userDTO.getUserTel())
						.userMail(userDTO.getUserMail())
						.userAddr1(userDTO.getUserAddr1())
						.userAddr2(userDTO.getUserAddr2())
						.userAddr3(userDTO.getUserAddr3())
						.build();

		model.addAttribute("user", userDTO);
		ModelAndView mv = new ModelAndView();
		
		//수정하는 로직이 없다
		userService.updateUser(user);
		
		//소셜로그인 참조하여 @GetMapping("/join") securityContext에 등록되어 있는 athenticationToken을 수정된 정보로 변경
		SecurityContext securityContext = SecurityContextHolder.getContext();
		UserDetails newUserInfo = userDetailsServiceImpl.loadUserByUsername(userDTO.getUserId());
		Authentication authentication = new UsernamePasswordAuthenticationToken(newUserInfo, null, newUserInfo.getAuthorities());
		securityContext.setAuthentication(authentication);
		session.setAttribute("SPRING_SERCURITY_CONTEXT", securityContext);
		
		mv.setViewName("mypage/changeInfo.html");
		mv.addObject("socialUser", user);
		
		return mv;
    }
}