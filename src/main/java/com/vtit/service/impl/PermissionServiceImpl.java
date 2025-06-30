package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.PermissionRepository;
import com.vtit.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService{
	private final PermissionRepository permissionRepository;
	
	public PermissionServiceImpl(PermissionRepository permissionRepository) {
		this.permissionRepository = permissionRepository;
	}
}
