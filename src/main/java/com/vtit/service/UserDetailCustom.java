package com.vtit.service;

import java.util.Collections;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.vtit.entity.Users;

@Component("userDetailsService")
public class UserDetailCustom implements UserDetailsService {

	private final UserService userService;

	public UserDetailCustom(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
	    Users user = this.userService.handleGetUserByUsername(username);

	    if (user == null) {
	        // Không dùng UsernameNotFoundException nữa
	        throw new BadCredentialsException("Thông tin đăng nhập sai");
	    }

	    return new User(
	        user.getUsername(),
	        user.getPassword(),
	        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
	    );
	}

}
