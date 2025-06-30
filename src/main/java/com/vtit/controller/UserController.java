package com.vtit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.entity.Users;
import com.vtit.service.UserService;

@RestController("/api/v1/library/user")
public class UserController {
	private final UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
//	@GetMapping("/{id}")
//	public Users getUserById(@PathVariable Integer id) {
//		return this.userService.find
//	}
}
