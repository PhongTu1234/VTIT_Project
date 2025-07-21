package com.vtit.dto.response.role;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRoleDTO {
    private Integer id;
    private String code;
    private String name;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;

}

