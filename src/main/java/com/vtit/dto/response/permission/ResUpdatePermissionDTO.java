package com.vtit.dto.response.permission;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdatePermissionDTO {
	private Integer id;
    private String code;
    private String name;
    private String updatedBy;
    private Instant updatedDate;
}