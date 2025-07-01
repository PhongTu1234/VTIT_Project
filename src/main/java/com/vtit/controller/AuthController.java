package com.vtit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.dto.RestResponseDTO;
import com.vtit.service.UserService;

@RestController("/api/auth")
public class AuthController {

	private final UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<RestResponseDTO<T>> AuthLogin() {
		return ResponseEntity<T>;
	}
}
