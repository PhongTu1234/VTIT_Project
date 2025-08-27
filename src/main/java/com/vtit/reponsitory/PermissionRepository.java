package com.vtit.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vtit.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {
	boolean existsByCodeAndMethodAndModule(String code, String method, String module);
	List<Permission> findByIsActiveTrue();
}
