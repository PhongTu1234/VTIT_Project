package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer> {

}
