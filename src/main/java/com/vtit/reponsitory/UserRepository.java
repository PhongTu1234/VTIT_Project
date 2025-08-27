package com.vtit.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vtit.entity.Roles;
import com.vtit.entity.Users;

public interface UserRepository extends JpaRepository<Users, Integer>, JpaSpecificationExecutor<Users> {
	boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByUsernameAndIdNot(String username, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByPhoneAndIdNot(String phone, Integer id);
    Users findByEmail(String email);
    Users findByUsername(String username);
    Users findByRefreshTokenAndEmail(String refreshToken, String email);
    @Query("""
    	    select distinct u from Users u
    	    left join fetch u.role r
    	    left join fetch r.permissions p
    	    where u.username = :login or u.email = :login
    	""")
    	Optional<Users> findUserWithRolesAndPermissions(@Param("login") String login);



}
