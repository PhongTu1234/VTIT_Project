package com.vtit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.ResCreateUserDTO;
import com.vtit.dto.ResUpdateUserDTO;
import com.vtit.dto.ResUserDTO;
import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Users;

public interface UserService {

	ResultPaginationDTO findAll(Specification<Users> spec, Pageable pageable);
	ResUserDTO findById(String id);
    Users create(Users user);
    ResUpdateUserDTO update(String id, Users user);
    void delete(String id);
    Users handleGetUserByUsernames(String email);
    Users handleGetUserByUsername(String username);
    ResCreateUserDTO convertToResCreateUserDTO(Users user);
    ResUserDTO convertToResUserDTO(Users user);
    ResUpdateUserDTO convertToResUpdateUserDTO(Users user);
    void updateUserToken(String email, String token);
    Users findByRefreshTokenAndEmail(String refreshToken, String email);
}
