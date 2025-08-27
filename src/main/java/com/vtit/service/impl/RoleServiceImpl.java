package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.permission.ReqPermissionIdDTO;
import com.vtit.dto.request.role.ReqCreateRoleDTO;
import com.vtit.dto.request.role.ReqUpdateRoleDTO;
import com.vtit.dto.response.role.ResCreateRoleDTO;
import com.vtit.dto.response.role.ResRoleDTO;
import com.vtit.dto.response.role.ResUpdateRoleDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Category;
import com.vtit.entity.Permission;
import com.vtit.entity.Roles;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.mapper.RoleMapper;
import com.vtit.reponsitory.PermissionRepository;
import com.vtit.reponsitory.RoleRepository;
import com.vtit.service.RoleService;
import com.vtit.utils.IdValidator;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;
	private final RoleMapper roleMapper;

	public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
		this.roleMapper = roleMapper;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Roles> spec, Pageable pageable) {
		Page<Roles> pageRoles = roleRepository.findAll(spec, pageable);

		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageRoles.getTotalPages());
		mt.setTotals((int) pageRoles.getTotalElements());

		ResultPaginationDTO rs = new ResultPaginationDTO();
		rs.setMeta(mt);
		rs.setResult(pageRoles.getContent());
		return rs;
	}

	@Override
	public ResRoleDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Roles role = roleRepository.findById(idInt)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy quyền với id = " + idInt));
		return roleMapper.toResRoleDTO(role);
	}

	@Override
	public ResCreateRoleDTO create(ReqCreateRoleDTO dto) {
		List<Integer> permissionIds = dto.getPermissions().stream()
			.map(p -> p.getId())
			.toList();

		List<Permission> permissions = permissionRepository.findAllById(permissionIds);

		if (permissions.size() != permissionIds.size()) {
			List<Integer> foundIds = permissions.stream().map(Permission::getId).toList();
			List<Integer> missingIds = permissionIds.stream().filter(id -> !foundIds.contains(id)).toList();
			throw new ResourceNotFoundException("Permission not found with ID(s): " + missingIds);
		}

		if (roleRepository.existsByName(dto.getName())) {
			throw new DuplicateResourceException("Role đã tồn tại");
		}

		Roles newRole = roleMapper.fromCreateDTO(dto);
		newRole.setPermissions((Set<Permission>) permissions);
		Roles savedRole = roleRepository.save(newRole);

		return roleMapper.toResCreateRoleDTO(savedRole);
	}

	@Override
	public ResUpdateRoleDTO update(ReqUpdateRoleDTO dto) {
		Roles existingRole = roleRepository.findById(dto.getId()).orElseThrow(
			() -> new ResourceNotFoundException("Không tìm thấy quyền với id = " + dto.getId()));

		if (dto.getName() != null && roleRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
			throw new DuplicateResourceException("Name '" + dto.getName() + "' đã tồn tại");
		}

		List<Integer> permissionIds = dto.getPermissions().stream()
			.map(p -> p.getId())
			.toList();

		List<Permission> permissions = permissionRepository.findAllById(permissionIds);

		if (permissions.size() != permissionIds.size()) {
			List<Integer> foundIds = permissions.stream().map(Permission::getId).toList();
			List<Integer> missingIds = permissionIds.stream().filter(id -> !foundIds.contains(id)).toList();
			throw new ResourceNotFoundException("Permission not found with ID(s): " + missingIds);
		}

		existingRole = roleMapper.fromUpdateDTO(dto, existingRole);
		existingRole.setPermissions((Set<Permission>) permissions);

		Roles savedRole = roleRepository.save(existingRole);
		return roleMapper.toResUpdateRoleDTO(savedRole);
	}

	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Roles role = roleRepository.findById(intId)
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy quyền với id = " + intId));
		roleRepository.delete(role);
	}
}