package com.vtit.service.impl;

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
import com.vtit.mapper.PermissionMapper;
import com.vtit.reponsitory.PermissionRepository;
import com.vtit.service.PermissionService;
import com.vtit.utils.IdValidator;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public ResultPaginationDTO findAll(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermission = permissionRepository.findAll(spec, pageable);

        List<ResPermissionDTO> permissionDTOs = pagePermission.getContent().stream()
            .map(permissionMapper::toResPermissionDTO)
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
        return permissionMapper.toResPermissionDTO(permission);
    }

    @Override
    public ResCreatePermissionDTO create(ReqCreatePermissionDTO dto) {
        if (permissionRepository.existsByCodeAndMethodAndModule(dto.getCode(), dto.getMethod(), dto.getModule())) {
            throw new DuplicateResourceException("Permission đã tồn tại");
        }

        Permission permission = permissionMapper.fromCreateDTO(dto);
        Permission saved = permissionRepository.save(permission);
        return permissionMapper.toResCreatePermissionDTO(saved);
    }

    @Override
    public ResUpdatePermissionDTO update(ReqUpdatePermissionDTO dto) {
        Permission existing = permissionRepository.findById(dto.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy permission với id = " + dto.getId()));

        if (dto.getCode() != null && !dto.getCode().equals(existing.getCode()) &&
            permissionRepository.existsByCodeAndMethodAndModule(dto.getCode(), dto.getMethod(), dto.getModule())) {
            throw new DuplicateResourceException("Permission đã tồn tại");
        }

        existing.setCode(dto.getCode());
        existing.setName(dto.getName());
        existing.setMethod(dto.getMethod());
        existing.setModule(dto.getModule());

        Permission updated = permissionRepository.save(existing);
        return permissionMapper.toResUpdatePermissionDTO(updated);
    }

    @Override
    public void delete(String id) {
        Integer idInt = IdValidator.validateAndParse(id);
        Permission permission = permissionRepository.findById(idInt)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy permission với id = " + idInt));
        permissionRepository.delete(permission);
    }
}
