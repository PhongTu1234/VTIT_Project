package com.vtit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.dto.LoginDTO;
import com.vtit.dto.RestResponseDTO;
import com.vtit.service.UserService;

@RestController("/api/auth")
public class AuthController {

	private final UserService userService;
	
	public AuthController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginDTO> AuthLogin(@RequestBody LoginDTO loginDTO) {
		return ResponseEntity.ok(loginDTO);
	}
}
