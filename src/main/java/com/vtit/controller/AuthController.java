package com.vtit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.vtit.dto.request.auth.ReqLoginDTO;
import com.vtit.dto.response.auth.ResLoginDTO;
import com.vtit.dto.response.permission.ResPermissionSummaryDTO;
import com.vtit.dto.response.role.ResRoleSummaryDTO;
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

    public AuthController(SecurityUtil securityUtil,
                          AuthenticationManager authenticationManager,
                          UserService userService) {
        this.securityUtil = securityUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@RequestBody ReqLoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return generateLoginResponse(authentication.getName(), loginDTO.getUsername());
    }

    @GetMapping("/account")
    @ApiMessage("Get current user account info")
    public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
        Optional<String> currentEmail = SecurityUtil.getCurrentUserLogin();
        if (currentEmail.isEmpty()) {
            throw new IdInvalidException("Bạn chưa đăng nhập");
        }

        Users currentUser = userService.handleGetUserByUsernames(currentEmail.get());
        if (currentUser == null) {
            throw new IdInvalidException("Người dùng không tồn tại");
        }

        ResLoginDTO.UserInfo userInfo = new ResLoginDTO.UserInfo();
        userInfo.setId(currentUser.getId());
        userInfo.setUsername(currentUser.getUsername());
        userInfo.setEmail(currentUser.getEmail());

        ResLoginDTO.UserGetAccount response = new ResLoginDTO.UserGetAccount();
        response.setUserInfo(userInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token")
    @ApiMessage("Renew access token by refresh token")
    public ResponseEntity<ResLoginDTO> refreshToken(
            @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {

        if (refreshToken.isBlank()) {
            throw new IdInvalidException("Bạn không có refresh token trong cookie, vui lòng đăng nhập lại");
        }

        Jwt decoded = securityUtil.checkValidRefreshToken(refreshToken);
        String email = decoded.getSubject();

        Users user = userService.findByRefreshTokenAndEmail(refreshToken, email);
        if (user == null) {
            throw new IdInvalidException("Refresh token không hợp lệ");
        }

        return generateLoginResponse(email, email);
    }

    @PostMapping("/logout")
    @ApiMessage("Logout current user")
    public ResponseEntity<Void> logout() {
        String email = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new IdInvalidException("Bạn chưa đăng nhập"));

        userService.updateUserToken(email, null);

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true).secure(true).path("/").maxAge(0).build();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }

    // ========== PRIVATE METHOD ========== //

    private ResponseEntity<ResLoginDTO> generateLoginResponse(String email, String username) {
        Users user = userService.handleGetUserByUsername(username);
        if (user == null) {
            throw new IdInvalidException("Tài khoản không tồn tại");
        }

        // Build user info
        ResLoginDTO.UserInfo userInfo = new ResLoginDTO.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setEmail(user.getEmail());

        // Build role info
        ResRoleSummaryDTO roleDTO = new ResRoleSummaryDTO();
        roleDTO.setId(user.getRole().getId());
        roleDTO.setName(user.getRole().getName());

        // Build permission list
        List<ResPermissionSummaryDTO> permissionList = user.getRole().getPermissions().stream()
                .map(permission -> {
                    ResPermissionSummaryDTO dto = new ResPermissionSummaryDTO();
                    dto.setName(permission.getName());
                    dto.setMethod(permission.getMethod());
                    dto.setModule(permission.getModule());
                    dto.setCode(permission.getCode());
                    return dto;
                }).toList();
        roleDTO.setPermissions(permissionList);
        userInfo.setRole(roleDTO);

        // Extract permission codes (đây là danh sách quyền thực tế)
        List<String> permissionCodes = user.getRole().getPermissions().stream()
                .map(p -> p.getCode()) // 👈 phải có field code trong Permission entity
                .toList();

        // Build response
        ResLoginDTO response = new ResLoginDTO();
        response.setUserInfo(userInfo);

        // Create tokens
        String accessToken = securityUtil.createAccessToken(email, response, permissionCodes);
        String refreshToken = securityUtil.createRefreshToken(username, response, permissionCodes);

        // Save refresh token
        userService.updateUserToken(username, refreshToken);

        // Set cookie
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(refreshTokenExpiration).build();

        response.setAccessToken(accessToken);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(response);
    }
}
