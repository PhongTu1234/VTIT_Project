package com.vtit.dto.response.permission;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPermissionDTO {
    private Integer id;
    private String code;
    private String name;
    private String method;
    private String module;
    private Instant createdDate;
    private String createdBy;
    private Instant updatedDate;
    private String updatedBy;
}
