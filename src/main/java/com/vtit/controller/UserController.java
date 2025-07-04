package com.vtit.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vtit.dto.RestResponseDTO;
import com.vtit.entity.Users;
import com.vtit.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    
    public UserController(UserService userService) {
    	this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<Users>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
    }

    @PostMapping
    public ResponseEntity<Users> create(@Valid @RequestBody Users user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Users> update(@PathVariable Integer id, @Valid @RequestBody Users user) {
        return ResponseEntity.ok(userService.update(id, user));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponseDTO<Object>> delete(@PathVariable Integer id) {
        userService.delete(id);

        RestResponseDTO<Object> response = new RestResponseDTO<>();
        response.setStatusCode(200);
        response.setMessage("Xóa người dùng thành công với id = " + id);
        return ResponseEntity.ok(response);
    }

}
