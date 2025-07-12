package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vtit.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer>, JpaSpecificationExecutor<Roles> {
	boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
}
