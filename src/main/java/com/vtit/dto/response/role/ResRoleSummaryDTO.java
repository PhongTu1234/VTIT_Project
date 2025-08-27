package com.vtit.dto.response.role;

import java.util.List;

import com.vtit.dto.response.permission.ResPermissionSummaryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResRoleSummaryDTO {
	private Integer id;
	private String name;
	private List<ResPermissionSummaryDTO> permissions;
}
