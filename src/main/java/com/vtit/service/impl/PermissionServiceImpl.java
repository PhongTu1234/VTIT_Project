package com.vtit.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.permission.ReqCreatePermissionDTO;
import com.vtit.dto.request.permission.ReqUpdatePermissionDTO;
import com.vtit.dto.response.permission.ResCreatePermissionDTO;
import com.vtit.dto.response.permission.ResPermissionDTO;
import com.vtit.dto.response.permission.ResUpdatePermissionDTO;
import com.vtit.entity.Permission;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.PermissionRepository;
import com.vtit.service.PermissionService;
import com.vtit.utils.IdValidator;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public ResultPaginationDTO findAll(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec, pageable);

        List<ResPermissionDTO> permissionDTOs = pagePermission.getContent().stream()
            .map(this::convertToResPermissionDTO)
            .collect(Collectors.toList());

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pagePermission.getTotalPages());
        meta.setTotals((int) pagePermission.getTotalElements());

        ResultPaginationDTO result = new ResultPaginationDTO();
        result.setMeta(meta);
        result.setResult(permissionDTOs);

        return result;
    }

    @Override
    public ResPermissionDTO findById(String id) {
        Integer idInt = IdValidator.validateAndParse(id);
        Permission permission = permissionRepository.findById(idInt)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy permission với id = " + idInt));
        return convertToResPermissionDTO(permission);
    }

    @Override
    public ResCreatePermissionDTO create(ReqCreatePermissionDTO dto) {
        if (permissionRepository.existsByCodeAndMethodAndModule(dto.getCode(), dto.getMethod(), dto.getModule())) {
            throw new DuplicateResourceException("Permission đã tồn tại");
        }

        Permission permission = new Permission();
        permission.setCode(dto.getCode());
        permission.setName(dto.getName());

        Permission saved = permissionRepository.save(permission);
        return convertToResCreatePermissionDTO(saved);
    }

    @Override
    public ResUpdatePermissionDTO update(ReqUpdatePermissionDTO dto) {
        Permission permission = permissionRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy permission với id = " + dto.getId()));

        if (dto.getCode() != null && !dto.getCode().equals(permission.getCode()) &&
            permissionRepository.existsByCodeAndMethodAndModule(dto.getCode(), dto.getMethod(), dto.getModule())) {
            throw new DuplicateResourceException("Permission đã tồn tại");
        }

        permission.setCode(dto.getCode());
        permission.setName(dto.getName());

        Permission updated = permissionRepository.save(permission);
        return convertToResUpdatePermissionDTO(updated);
    }

    @Override
    public void delete(String id) {
        Integer idInt = IdValidator.validateAndParse(id);
        Permission permission = permissionRepository.findById(idInt)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy permission với id = " + idInt));
        permissionRepository.delete(permission);
    }


    private ResPermissionDTO convertToResPermissionDTO(Permission permission) {
        ResPermissionDTO dto = new ResPermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setCreatedDate(permission.getCreatedDate());
        dto.setCreatedBy(permission.getCreatedBy());
        dto.setUpdatedDate(permission.getUpdatedDate());
        dto.setUpdatedBy(permission.getUpdatedBy());
        return dto;
    }

    private ResCreatePermissionDTO convertToResCreatePermissionDTO(Permission permission) {
        ResCreatePermissionDTO dto = new ResCreatePermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setCreatedDate(permission.getCreatedDate());
        dto.setCreatedBy(permission.getCreatedBy());
        return dto;
    }

    private ResUpdatePermissionDTO convertToResUpdatePermissionDTO(Permission permission) {
        ResUpdatePermissionDTO dto = new ResUpdatePermissionDTO();
        dto.setId(permission.getId());
        dto.setCode(permission.getCode());
        dto.setName(permission.getName());
        dto.setUpdatedDate(permission.getUpdatedDate());
        dto.setUpdatedBy(permission.getUpdatedBy());
        return dto;
    }
    public Permission convertToEntity(ReqCreatePermissionDTO dto) {
        Permission permission = new Permission();
        permission.setCode(dto.getCode());
        permission.setName(dto.getName());
        return permission;
    }
    public Permission convertToEntity(ReqUpdatePermissionDTO dto) {
        Permission permission = new Permission();
        permission.setId(dto.getId());
        permission.setCode(dto.getCode());
        permission.setName(dto.getName());
        return permission;
    }

}
