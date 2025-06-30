package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.RolePermissionRepository;
import com.vtit.service.RolePermissionService;

@Service
public class RolePermissionServiceImpl implements RolePermissionService{
	private final RolePermissionRepository rolePermissionRepository;
	
	public RolePermissionServiceImpl(RolePermissionRepository rolePermissionRepository) {
		this.rolePermissionRepository = rolePermissionRepository;
	}
}
