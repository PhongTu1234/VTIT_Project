package com.vtit.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.dto.request.auth.ReqLoginDTO;
import com.vtit.dto.response.auth.ResLoginDTO;
import com.vtit.entity.Users;
import com.vtit.exception.IdInvalidException;
import com.vtit.service.UserService;
import com.vtit.utils.SecurityUtil;
import com.vtit.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final SecurityUtil securityUtil;
	private final AuthenticationManager authenticationManager;
	private final UserService userService;

	@Value("${jwt.refresh-token-validity-seconds}")
	private Long refreshTokenExpiration;

	public AuthController(SecurityUtil securityUtil, AuthenticationManager authenticationManager,
			UserService userService) {
		this.securityUtil = securityUtil;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	public ResponseEntity<ResLoginDTO> generateLoginResponse(String email,
			String username) {
		ResLoginDTO res = new ResLoginDTO();
		
		Users userDB = userService.handleGetUserByUsername(username);

		ResLoginDTO.UserInfo userInfo = new ResLoginDTO.UserInfo(userDB.getId(), userDB.getUsername(), userDB.getEmail());
		res.setUserInfo(userInfo);

		// Tạo access token
		String accessToken = this.securityUtil.createAccessToken(email, userInfo);
		res.setAccessToken(accessToken);

		// Tạo refresh token
		String refreshToken = this.securityUtil.createRefreshToken(username, res);

		// Lưu refresh token vào DB
		this.userService.updateUserToken(username, refreshToken);

		// Tạo cookie
		ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken).httpOnly(true).secure(true).path("/")
				.maxAge(refreshTokenExpiration).build();

		// Trả về response chứa access token + cookie
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(res);
	}

	@PostMapping("/login")
	public ResponseEntity<ResLoginDTO> AuthLogin(@RequestBody ReqLoginDTO loginDTO) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getUsername(), loginDTO.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// set thông tin người dùng vào context
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return this.generateLoginResponse(authentication.getName(), loginDTO.getUsername());
	}

	@GetMapping("/account")
	@ApiMessage("fresh account")
	public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";

		Users currentUserDB = this.userService.handleGetUserByUsernames(email);
		ResLoginDTO.UserInfo userInfo = new ResLoginDTO.UserInfo();
		ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
		if (currentUserDB != null) {
			userInfo.setId(currentUserDB.getId());
			userInfo.setUsername(currentUserDB.getUsername());
			userInfo.setEmail(currentUserDB.getEmail());
			userGetAccount.setUserInfo(userInfo);
		}
		return ResponseEntity.ok(userGetAccount);
	}

	@GetMapping("/refresh-token")
	@ApiMessage("Get User by refresh token")
	public ResponseEntity<ResLoginDTO> getRefreshToken(
			@CookieValue(value = "refresh_token", required = false, defaultValue = "abc") String refreshToken) {
		if ("abc".equals(refreshToken)) {
			throw new IdInvalidException("bạn không có refresh token nào trong cookie, vui lòng đăng nhập lại");
		}
		Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
		String email = decodedToken.getSubject();

		Users currentUserDB = this.userService.findByRefreshTokenAndEmail(refreshToken, email);
		if (currentUserDB == null) {
			throw new IdInvalidException("Refresh token không hợp lệ");
		} else {
			return this.generateLoginResponse(email, email);
		}
	}
	
	@PostMapping("/logout")
	@ApiMessage("Logout User")
	public ResponseEntity<Void> logoutUser() throws IdInvalidException{
		String email = SecurityUtil.getCurrentUserLogin()
				.orElseThrow(() -> new IdInvalidException("Bạn chưa đăng nhập"));

		// Xoá refresh token khỏi cơ sở dữ liệu
		this.userService.updateUserToken(email, null);

		// Tạo cookie rỗng để xoá cookie hiện tại
		ResponseCookie cookie = ResponseCookie
				.from("refresh_token", null)
				.httpOnly(true)
				.secure(true)
				.path("/")
				.maxAge(0)
				.build();

		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
	}
}
