package com.vtit.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vtit.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer>, JpaSpecificationExecutor<Roles> {
	boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Integer id);
    @Query("SELECT DISTINCT r FROM Roles r " +
            "LEFT JOIN FETCH r.permissions " +
            "WHERE r.id = :roleId")
     Optional<Roles> findRoleWithPermissions(@Param("roleId") Integer roleId);
}
