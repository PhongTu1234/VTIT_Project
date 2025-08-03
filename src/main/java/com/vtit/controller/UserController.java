package com.vtit.controller;

import java.net.URISyntaxException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.User.ReqCreateUserDTO;
import com.vtit.dto.request.User.ReqUpdateUserDTO;
import com.vtit.dto.response.User.ResCreateUserDTO;
import com.vtit.dto.response.User.ResUpdateUserDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.entity.Users;
import com.vtit.service.UserService;
import com.vtit.utils.annotation.ApiMessage;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
    	this.userService = userService;
    }

    @GetMapping
    @ApiMessage("Fetch All Users")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Users> spec, Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("Fetch User by ID")
    public ResponseEntity<ResUserDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @ApiMessage("Create a New User")
    public ResponseEntity<ResCreateUserDTO> create(@Valid @RequestBody ReqCreateUserDTO dto,
    		@RequestPart("avatar") MultipartFile avatar) throws URISyntaxException, Exception {
        return ResponseEntity.ok(userService.create(dto, avatar));
    }

    @PutMapping
    @ApiMessage("Update a User")
    public ResponseEntity<ResUpdateUserDTO> update(@Valid @RequestBody ReqUpdateUserDTO dto,
    		@RequestPart("avatar") MultipartFile avatar) throws URISyntaxException, Exception {
        return ResponseEntity.ok(userService.update(dto,avatar));
    }

    @DeleteMapping("/{id}")
    @ApiMessage("Delete a User")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
