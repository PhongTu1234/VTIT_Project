package com.vtit.dto.response.role;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateRoleDTO {
	private Integer id;
    private String name;
    private String description;
    private String createdBy;
    private Instant createdDate;
}
