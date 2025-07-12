package com.vtit.controller;

import java.util.Optional;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Roles;
import com.vtit.service.RoleService;
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
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Roles> spec, Pageable pageable) {
		return ResponseEntity.ok(roleService.findAll(spec,pageable));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Roles> getById(@PathVariable String id) {
		return roleService.findById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new EntityNotFoundException("Không tìm thấy quyền với id = " + id));
	}

	@PostMapping
	public ResponseEntity<Roles> create(@Valid @RequestBody Roles role) {
		return ResponseEntity.status(HttpStatus.SC_CREATED).body(roleService.create(role));
	}

	@PutMapping
	public ResponseEntity<Roles> update(@Valid @RequestBody Roles role) {
		return ResponseEntity.ok(roleService.update(role));
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		roleService.delete(id);
	}

}