package com.vtit.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.role.ReqCreateRoleDTO;
import com.vtit.dto.request.role.ReqUpdateRoleDTO;
import com.vtit.dto.response.role.ResCreateRoleDTO;
import com.vtit.dto.response.role.ResRoleDTO;
import com.vtit.dto.response.role.ResUpdateRoleDTO;
import com.vtit.entity.Roles;

public interface RoleService {

	ResultPaginationDTO findAll(Specification<Roles> spec, Pageable pageable);
	ResRoleDTO findById(String id);
	ResCreateRoleDTO create(ReqCreateRoleDTO role);
	ResUpdateRoleDTO update(ReqUpdateRoleDTO role);
	void delete(String id);

}