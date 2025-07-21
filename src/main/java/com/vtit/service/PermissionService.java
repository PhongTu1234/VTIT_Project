package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.permission.ReqCreatePermissionDTO;
import com.vtit.dto.request.permission.ReqUpdatePermissionDTO;
import com.vtit.dto.response.permission.ResCreatePermissionDTO;
import com.vtit.dto.response.permission.ResPermissionDTO;
import com.vtit.dto.response.permission.ResUpdatePermissionDTO;
import com.vtit.entity.Permission;

public interface PermissionService {
	ResultPaginationDTO findAll(Specification<Permission> spec, Pageable pageable);

    ResPermissionDTO findById(String id);

    ResCreatePermissionDTO create(ReqCreatePermissionDTO dto);

    ResUpdatePermissionDTO update(ReqUpdatePermissionDTO dto);

    void delete(String id);
}
