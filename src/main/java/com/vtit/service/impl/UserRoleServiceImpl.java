package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.UserRepository;
import com.vtit.service.UserRoleService;

@Service
public class UserRoleServiceImpl implements UserRoleService{
	private final UserRepository userRepository;
	
	public UserRoleServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
}
