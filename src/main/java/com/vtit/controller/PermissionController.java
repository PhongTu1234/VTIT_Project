package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.permission.ReqCreatePermissionDTO;
import com.vtit.dto.request.permission.ReqUpdatePermissionDTO;
import com.vtit.dto.response.permission.ResCreatePermissionDTO;
import com.vtit.dto.response.permission.ResUpdatePermissionDTO;
import com.vtit.dto.response.permission.ResPermissionDTO;
import com.vtit.entity.Permission;
import com.vtit.service.PermissionService;
import com.vtit.utils.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Fetch All Permissions")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Permission> spec, Pageable pageable) {
        return ResponseEntity.ok(permissionService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Fetch Permission by ID")
    public ResponseEntity<ResPermissionDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @PostMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Create a New Permission")
    public ResponseEntity<ResCreatePermissionDTO> create(@Valid @RequestBody ReqCreatePermissionDTO dto) {
        return ResponseEntity.ok(permissionService.create(dto));
    }

    @PutMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Update a Permission")
    public ResponseEntity<ResUpdatePermissionDTO> update(@Valid @RequestBody ReqUpdatePermissionDTO dto) {
        return ResponseEntity.ok(permissionService.update(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Delete a Permission")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
