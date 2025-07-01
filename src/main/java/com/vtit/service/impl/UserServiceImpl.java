package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vtit.entity.Users;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	private final UserRepository userRepository;
	
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<Users> findById(Integer id) {
        return userRepository.findById(id);
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

        return userRepository.save(user);
    }

    @Override
    public Users update(Integer id, Users updatedUser) {
        // Kiểm tra ID hợp lệ
        if (id == null || id <= 0) {
            throw new IdInvalidException("Id phải lớn hơn 0");
        }

        // Kiểm tra User có tồn tại không
        Users existingUser = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + id));

        // Kiểm tra trùng username (loại trừ chính user đang update)
        if (userRepository.existsByUsernameAndIdNot(updatedUser.getUsername(), id)) {
            throw new DuplicateResourceException("Username '" + updatedUser.getUsername() + "' đã tồn tại");
        }

        // Kiểm tra trùng email
        if (updatedUser.getEmail() != null &&
            userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), id)) {
            throw new DuplicateResourceException("Email '" + updatedUser.getEmail() + "' đã tồn tại");
        }

        // Kiểm tra trùng phone
        if (updatedUser.getPhone() != null &&
            userRepository.existsByPhoneAndIdNot(updatedUser.getPhone(), id)) {
            throw new DuplicateResourceException("Phone '" + updatedUser.getPhone() + "' đã tồn tại");
        }

        // Cập nhật thông tin
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setBirthday(updatedUser.getBirthday());
        existingUser.setUpdatedBy(updatedUser.getUpdatedBy()); // Optional

        return userRepository.save(existingUser);
    }



    @Override
    public void delete(Integer id) {
        Users user = userRepository.findById(id)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy người dùng với id = " + id));
        user.setIsDeleted(true);
        userRepository.save(user);
    }
}
