package com.vtit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Users;

public interface UserService {

	ResultPaginationDTO findAll(Specification<Users> spec, Pageable pageable);
    Optional<Users> findById(String id);
    Users create(Users user);
    Users update(String id, Users user);
    void delete(String id);
    Users handleGetUserByUsername(String username);
}
