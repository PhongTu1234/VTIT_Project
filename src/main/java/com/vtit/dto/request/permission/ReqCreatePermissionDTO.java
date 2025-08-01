package com.vtit.dto.request.permission;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreatePermissionDTO {
    private String code;
    private String name;
    private	String method;
    private String module;
}
