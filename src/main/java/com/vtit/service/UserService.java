package com.vtit.service;

import java.util.Optional;

import com.vtit.entity.Users;

public interface UserService {

	Optional<Users> findById(Integer id);
}
