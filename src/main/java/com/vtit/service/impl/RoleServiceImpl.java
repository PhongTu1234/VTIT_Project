package com.vtit.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.Meta;
import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Roles;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.RoleRepository;
import com.vtit.service.RoleService;
import com.vtit.utils.IdValidator;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Roles> spec, Pageable pageable) {
		Page<Roles> pageRoles = roleRepository.findAll(spec, pageable);

		Meta meta = new Meta();
		meta.setPage(pageRoles.getNumber() + 1);
		meta.setPageSize(pageRoles.getSize());
		meta.setPages(pageRoles.getTotalPages());
		meta.setTotals((int) pageRoles.getTotalElements());

		ResultPaginationDTO rs = new ResultPaginationDTO();
		rs.setMeta(meta);
		rs.setResult(pageRoles.getContent());
		return rs;
	}

	@Override
	public Optional<Roles> findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		return roleRepository.findById(idInt);
	}

	@Override
	public Roles create(Roles role) {
		if (roleRepository.existsByName(role.getName())) {
			throw new DuplicateResourceException("Name '" + role.getName() + "' đã tồn tại");
		}
		return roleRepository.save(role);
	}

	@Override
	public Roles update(Roles updatedRole) {
		Roles existingRole = roleRepository.findById(updatedRole.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy quyền với id = " + updatedRole.getId()));

		if (updatedRole.getCode() != null
				&& roleRepository.existsByNameAndIdNot(updatedRole.getName(), updatedRole.getId())) {
			throw new DuplicateResourceException("Name '" + updatedRole.getName() + "' đã tồn tại");
		}

		if (updatedRole.getName() != null)
			existingRole.setName(updatedRole.getName());
		if (updatedRole.getCode() != null)
			existingRole.setCode(updatedRole.getCode());

		return roleRepository.save(existingRole);
	}

	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Roles role = roleRepository.findById(intId)
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy quyền với id = " + intId));
		roleRepository.delete(role);
	}
}