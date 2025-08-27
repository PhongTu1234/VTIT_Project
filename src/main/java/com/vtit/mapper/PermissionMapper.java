package com.vtit.mapper;

import com.vtit.dto.request.permission.ReqCreatePermissionDTO;
import com.vtit.dto.request.permission.ReqUpdatePermissionDTO;
import com.vtit.dto.response.permission.ResCreatePermissionDTO;
import com.vtit.dto.response.permission.ResPermissionDTO;
import com.vtit.dto.response.permission.ResUpdatePermissionDTO;
import com.vtit.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public ResPermissionDTO toResPermissionDTO(Permission permission) {
        ResPermissionDTO dto = new ResPermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setCreatedDate(permission.getCreatedDate());
        dto.setCreatedBy(permission.getCreatedBy());
        dto.setUpdatedDate(permission.getUpdatedDate());
        dto.setUpdatedBy(permission.getUpdatedBy());
        return dto;
    }

    public ResCreatePermissionDTO toResCreatePermissionDTO(Permission permission) {
        ResCreatePermissionDTO dto = new ResCreatePermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setCreatedDate(permission.getCreatedDate());
        dto.setCreatedBy(permission.getCreatedBy());
        return dto;
    }

    public ResUpdatePermissionDTO toResUpdatePermissionDTO(Permission permission) {
        ResUpdatePermissionDTO dto = new ResUpdatePermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setUpdatedDate(permission.getUpdatedDate());
        dto.setUpdatedBy(permission.getUpdatedBy());
        return dto;
    }

    public Permission fromCreateDTO(ReqCreatePermissionDTO dto) {
        Permission permission = new Permission();
        permission.setCode(dto.getCode());
        permission.setName(dto.getName());
        permission.setMethod(dto.getMethod());
        permission.setModule(dto.getModule());
        return permission;
    }

    public Permission fromUpdateDTO(ReqUpdatePermissionDTO dto) {
        Permission permission = new Permission();
        permission.setId(dto.getId());
        permission.setCode(dto.getCode());
        permission.setName(dto.getName());
        permission.setMethod(dto.getMethod());
        permission.setModule(dto.getModule());
        return permission;
    }
}
