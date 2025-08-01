package com.vtit.dto.response.role;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResUpdateRoleDTO {
	private Integer id;
    private String name;
    private String description;
    private String updatedBy;
    private Instant updatedDate;
}
