package com.ezen.makingbaking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezen.makingbaking.service.reser.ReserService;

@RestController
@RequestMapping("/reser")
public class ReserController {
	@Autowired
	private ReserService reserService;
}
