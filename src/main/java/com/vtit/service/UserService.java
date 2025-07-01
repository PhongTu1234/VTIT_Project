package com.vtit.service;

import java.util.List;
import java.util.Optional;

import com.vtit.entity.Users;

public interface UserService {

	List<Users> findAll();
    Optional<Users> findById(Integer id);
    Users create(Users user);
    Users update(Integer id, Users user);
    void delete(Integer id);
}
