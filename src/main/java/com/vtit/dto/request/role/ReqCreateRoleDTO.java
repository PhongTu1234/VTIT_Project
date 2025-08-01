package com.vtit.dto.request.role;

import java.util.List;

import com.vtit.dto.request.permission.ReqPermissionIdDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateRoleDTO {
    private String name;
    private String description;
    private List<ReqPermissionIdDTO> permissions;
}
