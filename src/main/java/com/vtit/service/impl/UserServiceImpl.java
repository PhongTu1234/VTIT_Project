package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vtit.entity.Users;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.UserService;
import com.vtit.utils.IdValidator;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	
	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findById(String id) {
    	Integer idInt = IdValidator.validateAndParse(id);
        return userRepository.findById(idInt);
    }

    @Override
    public Users create(Users user) {
    	if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("Username '" + user.getUsername() + "' already exists");
        }

        if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' already exists");
        }

        if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
            throw new DuplicateResourceException("Phone '" + user.getPhone() + "' already exists");
        }
        
        String hashPasssword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(hashPasssword);

        return userRepository.save(user);
    }

    @Override
    public Users update(String id, Users updatedUser) {
    	Integer intId = IdValidator.validateAndParse(id);

        // Tìm user theo ID
        Users existingUser = userRepository.findById(intId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + intId));

        // Kiểm tra trùng username
        if (updatedUser.getUsername() != null &&
            userRepository.existsByUsernameAndIdNot(updatedUser.getUsername(), intId)) {
            throw new DuplicateResourceException("Username '" + updatedUser.getUsername() + "' đã tồn tại");
        }

        // Kiểm tra trùng email
        if (updatedUser.getEmail() != null &&
            userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), intId)) {
            throw new DuplicateResourceException("Email '" + updatedUser.getEmail() + "' đã tồn tại");
        }

        // Kiểm tra trùng phone
        if (updatedUser.getPhone() != null &&
            userRepository.existsByPhoneAndIdNot(updatedUser.getPhone(), intId)) {
            throw new DuplicateResourceException("Phone '" + updatedUser.getPhone() + "' đã tồn tại");
        }

        // Hash lại mật khẩu nếu có
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            String hashPassword = this.passwordEncoder.encode(updatedUser.getPassword());
            existingUser.setPassword(hashPassword);
        }

        // Cập nhật thông tin (nếu có truyền lên)
        if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
        if (updatedUser.getFullname() != null) existingUser.setFullname(updatedUser.getFullname());
        if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhone() != null) existingUser.setPhone(updatedUser.getPhone());
        if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
        if (updatedUser.getBirthday() != null) existingUser.setBirthday(updatedUser.getBirthday());
        if (updatedUser.getUpdatedBy() != null) existingUser.setUpdatedBy(updatedUser.getUpdatedBy());

        return userRepository.save(existingUser);
    }

    
    @Override
    public void delete(String id) {
        Integer intId = IdValidator.validateAndParse(id);
        Users user = userRepository.findById(intId)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy người dùng với id = " + intId));
        userRepository.delete(user);
    }

	@Override
	public Users handleGetUserByUsername(String username) {
		// TODO Auto-generated method stub
		return this.userRepository.findByEmail(username);
	}
}
