package com.vtit.service;

import java.net.URISyntaxException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.User.ReqCreateUserDTO;
import com.vtit.dto.request.User.ReqUpdateUserDTO;
import com.vtit.dto.response.User.ResCreateUserDTO;
import com.vtit.dto.response.User.ResUpdateUserDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.entity.Users;

public interface UserService {
    
    ResultPaginationDTO findAll(Specification<Users> spec, Pageable pageable);
    
    ResUserDTO findById(String id);
    
    ResCreateUserDTO create(ReqCreateUserDTO reqCreateUserDTO, MultipartFile avatar)  throws URISyntaxException, Exception; // Trả DTO đã tạo

    ResUpdateUserDTO update(ReqUpdateUserDTO user, MultipartFile avatar)  throws URISyntaxException, Exception; // Sửa lại dùng request DTO
    
    void delete(String id);
    
    Users handleGetUserByUsername(String email);
    
    Users handleGetUserByUsernames(String username);

    void updateUserToken(String email, String token);

    Users findByRefreshTokenAndEmail(String refreshToken, String email);
    
    Users handleGetUserByUsernamea(String username);

}
