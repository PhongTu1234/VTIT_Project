package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}
