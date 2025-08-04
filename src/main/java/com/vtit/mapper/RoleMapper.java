package com.vtit.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vtit.dto.request.role.ReqCreateRoleDTO;
import com.vtit.dto.request.role.ReqUpdateRoleDTO;
import com.vtit.dto.response.role.ResCreateRoleDTO;
import com.vtit.dto.response.role.ResRoleDTO;
import com.vtit.dto.response.role.ResUpdateRoleDTO;
import com.vtit.entity.Roles;

@Component
public class RoleMapper {

    public ResRoleDTO toResRoleDTO(Roles role) {
        if (role == null) return null;
        ResRoleDTO dto = new ResRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setCreatedDate(role.getCreatedDate());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setUpdatedDate(role.getUpdatedDate());
        return dto;
    }

    public ResCreateRoleDTO toResCreateRoleDTO(Roles role) {
        if (role == null) return null;
        ResCreateRoleDTO dto = new ResCreateRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setCreatedDate(role.getCreatedDate());
        return dto;
    }

    public ResUpdateRoleDTO toResUpdateRoleDTO(Roles role) {
        if (role == null) return null;
        ResUpdateRoleDTO dto = new ResUpdateRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setUpdatedDate(role.getUpdatedDate());
        return dto;
    }

    public Roles fromCreateDTO(ReqCreateRoleDTO dto) {
        if (dto == null) return null;
        Roles role = new Roles();
        role.setName(dto.getName());
        role.setDescriptions(dto.getDescription());
        return role;
    }

    public Roles fromUpdateDTO(ReqUpdateRoleDTO dto, Roles role) {
        if (dto == null || role == null) return null;
        role.setName(dto.getName());
        if (dto.getDescription() != null) {
            role.setDescriptions(dto.getDescription());
        }
        return role;
    }
}
