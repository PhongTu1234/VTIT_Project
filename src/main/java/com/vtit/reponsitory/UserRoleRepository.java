package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

}
