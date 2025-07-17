package com.vtit.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.entity.Roles;

public interface RoleService {

	ResultPaginationDTO findAll(Specification<Roles> spec, Pageable pageable);
	Optional<Roles> findById(String id);
	Roles create(Roles role);
	Roles update(Roles role);
	void delete(String id);

}