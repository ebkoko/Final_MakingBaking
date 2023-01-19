package com.ezen.makingbaking.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ezen.makingbaking.dto.ResponseDTO;
import com.ezen.makingbaking.dto.UserDTO;
import com.ezen.makingbaking.entity.User;
import com.ezen.makingbaking.service.user.UserService;


@RestController
@RequestMapping("/main")
public class MainController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/main")
	public ModelAndView mainView(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("/main.html");
		
		
		if(request.getParameter("msg") != null && !request.getParameter("msg").equals("")) {
				mv.addObject("msg", "joinSuccess");
		}
		
		if(request.getParameter("quitMsg") != null && !request.getParameter("quitMsg").equals("")) {
			mv.addObject("quitMsg", request.getParameter("quitMsg").toString());
		}
		return mv;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(UserDTO userDTO, HttpSession session) {
		ResponseDTO<Map<String, String>> responseDTO = new ResponseDTO<>();
		Map<String, String> returnMap = new HashMap<String, String>();
		try {
			User user = User.builder()
							.userId(userDTO.getUserId())
							.userPw(userDTO.getUserPw())
							.build();
			
			User checkedUser = userService.idcheck(user);
			
			if(checkedUser == null) {
				returnMap.put("msg", "idFail");
			} else {
				if(!checkedUser.getUserPw().equals(userDTO.getUserPw())) {
					returnMap.put("msg", "pwFail");
				} else {
					UserDTO loginUser = UserDTO.builder()
											   .userId(userDTO.getUserId())
											   .userName(userDTO.getUserName())
											   .userNo(userDTO.getUserNo())
											   .userBirth(userDTO.getUserBirth())
											   .userGender(userDTO.getUserGender())
											   .userTel(userDTO.getUserTel())
											   .userMail(userDTO.getUserMail())
											   .userAddr1(userDTO.getUserAddr1())
											   .userAddr2(userDTO.getUserAddr2())
											   .userAddr3(userDTO.getUserAddr3())
											   .build();
					
					session.setAttribute("loginUser", loginUser);
					returnMap.put("msg", "loginSuccess");
				}
			}
			
			responseDTO.setItem(returnMap);
			
			return ResponseEntity.ok().body(responseDTO);
		} catch(Exception e) {
			responseDTO.setErrorMessage(e.getMessage());
			return ResponseEntity.badRequest().body(responseDTO);
		}
	}
	
	
	
}
