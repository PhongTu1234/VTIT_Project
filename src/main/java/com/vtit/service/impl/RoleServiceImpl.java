package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.RoleRepository;
import com.vtit.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService{
	private final RoleRepository roleRepository;
	
	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
}
