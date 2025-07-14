package com.vtit.controller;

import java.util.Optional;

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
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.ResCreateUserDTO;
import com.vtit.dto.ResUpdateUserDTO;
import com.vtit.dto.ResUserDTO;
import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Users;
import com.vtit.service.UserService;
import com.vtit.utils.annotation.ApiMessage;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
    	this.userService = userService;
    }

    @GetMapping
    @ApiMessage("Fetch All Api")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Users> spec, Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @ApiMessage("fetch User by ID")
    public ResponseEntity<ResUserDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PostMapping
    @ApiMessage("Create new a User")
    public ResponseEntity<ResCreateUserDTO> create(@Valid @RequestBody Users user) {
        return ResponseEntity.ok(userService.convertToResCreateUserDTO(user));
    }

    @PutMapping("/{id}")
    @ApiMessage("Update a User")
    public ResponseEntity<ResUpdateUserDTO> update(@PathVariable String id, @Valid @RequestBody Users user) {
        return ResponseEntity.ok(userService.update(id, user));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessage("Delete a User")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

}
