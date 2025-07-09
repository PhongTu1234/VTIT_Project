package com.vtit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.dto.LoginDTO;
import com.vtit.dto.ResLoginDTO;
import com.vtit.dto.RestResponseDTO;
import com.vtit.service.UserService;
import com.vtit.utils.SecurityUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final SecurityUtil securityUtil;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	public AuthController(SecurityUtil securityUtil,
			AuthenticationManagerBuilder authenticationManagerBuilder) {
		this.securityUtil = securityUtil;
		this.authenticationManagerBuilder = authenticationManagerBuilder;
	}

	@PostMapping("/login")
	public ResponseEntity<ResLoginDTO> AuthLogin(@RequestBody LoginDTO loginDTO) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		String access_token = this.securityUtil.createToken(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		ResLoginDTO res = new ResLoginDTO();
		res.setAccessToken(access_token);
		return ResponseEntity.ok(res);
	}
}
