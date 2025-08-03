package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;
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
import com.vtit.reponsitory.PermissionRepository;
import com.vtit.reponsitory.RoleRepository;
import com.vtit.service.RoleService;
import com.vtit.utils.IdValidator;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final PermissionRepository permissionRepository;

	public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;
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
		return convertToResRoleDTO(role);
	}

	@Override
	public ResCreateRoleDTO create(ReqCreateRoleDTO dto) {
//		if (roleRepository.existsByName(role.getName())) {
//			throw new DuplicateResourceException("Name '" + role.getName() + "' đã tồn tại");
//		}
//		return convertToResCreateRoleDTO(roleRepository.save(convertToEntity(role)));
		List<ReqPermissionIdDTO> permissionDTOs = dto.getPermissions();

		List<Integer> permissionIds = permissionDTOs.stream()
		    .map(ReqPermissionIdDTO::getId)
		    .collect(Collectors.toList());
		
		List<Permission> permissions = permissionRepository.findAllById(permissionIds);

	    if (permissions.size() != permissionIds.size()) {
	        List<Integer> foundIds = permissions.stream()
	                                           .map(Permission::getId)
	                                           .toList();
	        List<Integer> missingIds = permissionIds.stream()
	                                      .filter(id -> !foundIds.contains(id))
	                                      .toList();
	        throw new ResourceNotFoundException("Permission not found with ID(s): " + missingIds);
	    }

	    Roles newRole = convertToEntity(dto);
	    newRole.setPermission(permissions);
		if (!roleRepository.existsByName(dto.getName())) {
			throw new DuplicateResourceException("Role đã tồn tại");
		}

		Roles savedRole = roleRepository.save(newRole);

	    return convertToResCreateRoleDTO(savedRole);
	}

	@Override
	public ResUpdateRoleDTO update(ReqUpdateRoleDTO updatedRole) {
		// Kiểm tra Role tồn tại
		Roles existingRole = roleRepository.findById(updatedRole.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy quyền với id = " + updatedRole.getId()));

		if (updatedRole.getName() != null
				&& roleRepository.existsByNameAndIdNot(updatedRole.getName(), updatedRole.getId())) {
			throw new DuplicateResourceException("Name '" + updatedRole.getName() + "' đã tồn tại");
		}
		
	    // Kiểm tra danh mục (Permission) có tồn tại không
		List<ReqPermissionIdDTO> permissionDTOs = updatedRole.getPermissions();

		List<Integer> permissionIds = permissionDTOs.stream()
		    .map(ReqPermissionIdDTO::getId)
		    .collect(Collectors.toList());
		
		List<Permission> permissions = permissionRepository.findAllById(permissionIds);

	    if (permissions.size() != permissionIds.size()) {
	        List<Integer> foundIds = permissions.stream()
	                                           .map(Permission::getId)
	                                           .toList();
	        List<Integer> missingIds = permissionIds.stream()
	                                      .filter(id -> !foundIds.contains(id))
	                                      .toList();
	        throw new ResourceNotFoundException("Permission not found with ID(s): " + missingIds);
	    }
	    
	    // Cập nhật thông tin
	    Roles updateRole = convertToEntity(updatedRole, existingRole);
	    Roles savedBook = roleRepository.save(updateRole);
	    return convertToResUpdateRoleDTO(savedBook);
	}

	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Roles role = roleRepository.findById(intId)
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy quyền với id = " + intId));
		roleRepository.delete(role);
	}
	
	public ResRoleDTO convertToResRoleDTO(Roles role) {
        if (role == null) {
            return null;
        }

        ResRoleDTO dto = new ResRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setCreatedDate(role.getCreatedDate());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setUpdatedDate(role.getUpdatedDate());

        return dto;
    }
	
	public ResCreateRoleDTO convertToResCreateRoleDTO(Roles role) {
        if (role == null) {
            return null;
        }

        ResCreateRoleDTO dto = new ResCreateRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setCreatedDate(role.getCreatedDate());

        return dto;
    }
	
	public ResUpdateRoleDTO convertToResUpdateRoleDTO(Roles role) {
        if (role == null) {
            return null;
        }

        ResUpdateRoleDTO dto = new ResUpdateRoleDTO();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setDescription(role.getDescriptions());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setUpdatedDate(role.getUpdatedDate());

        return dto;
    }
	
	public Roles convertToEntity(ReqCreateRoleDTO dto) {
        if (dto == null) {
            return null;
        }

        Roles role = new Roles();
        role.setName(dto.getName());
        role.setDescriptions(dto.getDescription());

        return role;
    }
	
	public Roles convertToEntity(ReqUpdateRoleDTO dto, Roles existingRole) {
        if (dto == null || existingRole == null) {
            return null;
        }

        existingRole.setName(dto.getName());
        if (dto.getDescription() != null)
			existingRole.setDescriptions(dto.getDescription());

        return existingRole;
    }
}