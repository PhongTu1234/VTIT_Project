package com.vtit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vtit.entity.Users;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Optional<Users> findById(Integer id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}
