package com.vtit.service;

import java.util.List;
import java.util.Optional;

import com.vtit.entity.Users;

public interface UserService {

	List<Users> findAll();
    Optional<Users> findById(String id);
    Users create(Users user);
    Users update(String id, Users user);
    void delete(String id);
    Users handleGetUserByUsername(String username);
}
