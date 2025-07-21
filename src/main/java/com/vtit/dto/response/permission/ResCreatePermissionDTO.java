package com.vtit.dto.response.permission;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreatePermissionDTO {
	private Integer id;
    private String code;
    private String name;
    private String createdBy;
    private Instant createdDate;
}