package com.vtit.controller;

import java.util.Optional;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.role.ReqCreateRoleDTO;
import com.vtit.dto.request.role.ReqUpdateRoleDTO;
import com.vtit.dto.response.role.ResCreateRoleDTO;
import com.vtit.dto.response.role.ResRoleDTO;
import com.vtit.dto.response.role.ResUpdateRoleDTO;
import com.vtit.entity.Roles;
import com.vtit.service.RoleService;
import com.vtit.utils.annotation.ApiMessage;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/library/role")
public class RoleController {

	private final RoleService roleService;

	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Get list of roles")
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Roles> spec, Pageable pageable) {
		return ResponseEntity.ok(roleService.findAll(spec,pageable));
	}

	@GetMapping("/{id}")
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Get role by id")
	public ResponseEntity<ResRoleDTO> getById(@PathVariable String id) {
		return ResponseEntity.ok(roleService.findById(id));
	}

	@PostMapping
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Create new role")
	public ResponseEntity<ResCreateRoleDTO> create(@Valid @RequestBody ReqCreateRoleDTO role) {
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(roleService.create(role));
	}

	@PutMapping
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Update role")
	public ResponseEntity<ResUpdateRoleDTO> update(@Valid @RequestBody ReqUpdateRoleDTO role) {
		return ResponseEntity.ok(roleService.update(role));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Delete role by id")
	public void delete(@PathVariable String id) {
		roleService.delete(id);
	}

}