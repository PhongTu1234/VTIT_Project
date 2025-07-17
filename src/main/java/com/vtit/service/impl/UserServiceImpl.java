package com.vtit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.User.ReqCreateUserDTO;
import com.vtit.dto.request.User.ReqUpdateUserDTO;
import com.vtit.dto.response.User.ResCreateUserDTO;
import com.vtit.dto.response.User.ResUpdateUserDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.entity.Users;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.UserService;
import com.vtit.utils.IdValidator;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Users> spec, Pageable pageable) {
		Page<Users> pageUser = userRepository.findAll(spec, pageable);

		List<ResUserDTO> userDTOs = pageUser.getContent().stream()
			.map(this::convertToResUserDTO)
			.collect(Collectors.toList());

		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(pageUser.getTotalPages());
		meta.setTotals((int) pageUser.getTotalElements());

		ResultPaginationDTO result = new ResultPaginationDTO();
		result.setMeta(meta);
		result.setResult(userDTOs);

		return result;
	}

	@Override
	public ResUserDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Users user = userRepository.findById(idInt)
			.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + idInt));
		return convertToResUserDTO(user);
	}

	@Override
	public ResCreateUserDTO create(ReqCreateUserDTO user) {
		// Kiểm tra trùng username
		if (userRepository.existsByUsername(user.getUsername())) {
			throw new DuplicateResourceException("Username '" + user.getUsername() + "' đã tồn tại");
		}
		// Kiểm tra trùng email
		if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
			throw new DuplicateResourceException("Email '" + user.getEmail() + "' đã tồn tại");
		}
		// Kiểm tra trùng phone
		if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
			throw new DuplicateResourceException("Phone '" + user.getPhone() + "' đã tồn tại");
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return convertToResCreateUserDTO(userRepository.save(convertToEntity(user)));
	}

	@Override
	public ResUpdateUserDTO update(ReqUpdateUserDTO updatedUser) {
		Users existingUser = userRepository.findById(updatedUser.getId())
			.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + updatedUser.getId()));

		// Kiểm tra trùng thông tin
		if (updatedUser.getUsername() != null &&
			userRepository.existsByUsernameAndIdNot(updatedUser.getUsername(), updatedUser.getId())) {
			throw new DuplicateResourceException("Username '" + updatedUser.getUsername() + "' đã tồn tại");
		}
		if (updatedUser.getEmail() != null &&
			userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), updatedUser.getId())) {
			throw new DuplicateResourceException("Email '" + updatedUser.getEmail() + "' đã tồn tại");
		}
		if (updatedUser.getPhone() != null &&
			userRepository.existsByPhoneAndIdNot(updatedUser.getPhone(), updatedUser.getId())) {
			throw new DuplicateResourceException("Phone '" + updatedUser.getPhone() + "' đã tồn tại");
		}

		// Cập nhật mật khẩu nếu có
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		// Cập nhật thông tin
		if (updatedUser.getUsername() != null) existingUser.setUsername(updatedUser.getUsername());
		if (updatedUser.getFullname() != null) existingUser.setFullname(updatedUser.getFullname());
		if (updatedUser.getEmail() != null) existingUser.setEmail(updatedUser.getEmail());
		if (updatedUser.getPhone() != null) existingUser.setPhone(updatedUser.getPhone());
		if (updatedUser.getAddress() != null) existingUser.setAddress(updatedUser.getAddress());
		if (updatedUser.getBirthday() != null) existingUser.setBirthday(updatedUser.getBirthday());
		if (updatedUser.getUpdatedBy() != null) existingUser.setUpdatedBy(updatedUser.getUpdatedBy());

		userRepository.save(existingUser);
		return convertToResUpdateUserDTO(existingUser);
	}

	@Override
	public void delete(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Users user = userRepository.findById(idInt)
			.orElseThrow(() -> new IdInvalidException("Không tìm thấy người dùng với id = " + idInt));
		userRepository.delete(user);
	}

	@Override
	public Users handleGetUserByUsername(String username) {
		return userRepository.findByEmail(username);
	}

	@Override
	public Users handleGetUserByUsernames(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public void updateUserToken(String email, String token) {
		Users user = handleGetUserByUsername(email);
		if (user != null) {
			user.setRefreshToken(token);
			userRepository.save(user);
		}
	}

	@Override
	public Users findByRefreshTokenAndEmail(String refreshToken, String email) {
		return userRepository.findByRefreshTokenAndEmail(refreshToken, email);
	}

	public ResCreateUserDTO convertToResCreateUserDTO(Users user) {
		ResCreateUserDTO dto = new ResCreateUserDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setFullname(user.getFullname());
		dto.setPhone(user.getPhone());
		dto.setAddress(user.getAddress());
		dto.setBirthday(user.getBirthday());
		dto.setCreateAt(user.getCreatedDate().toString());
		return dto;
	}

	public ResUserDTO convertToResUserDTO(Users user) {
		ResUserDTO dto = new ResUserDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setFullname(user.getFullname());
		dto.setPhone(user.getPhone());
		dto.setAddress(user.getAddress());
		dto.setBirthday(user.getBirthday());
		dto.setCreatedBy(user.getCreatedBy());
		dto.setCreateAt(user.getCreatedDate());
		dto.setUpdatedBy(user.getUpdatedBy());
		dto.setUpdateAt(user.getUpdatedDate());
		return dto;
	}

	public ResUpdateUserDTO convertToResUpdateUserDTO(Users user) {
		ResUpdateUserDTO dto = new ResUpdateUserDTO();
		dto.setId(user.getId());
		dto.setUsername(user.getUsername());
		dto.setFullname(user.getFullname());
		dto.setEmail(user.getEmail());
		dto.setPhone(user.getPhone());
		dto.setAddress(user.getAddress());
		dto.setBirthday(user.getBirthday());
		dto.setUpdatedAt(user.getUpdatedDate());
		return dto;
	}
	
	public Users convertToEntity(ReqCreateUserDTO dto) {
	    Users user = new Users();
	    user.setUsername(dto.getUsername());
	    user.setPassword(dto.getPassword());
	    user.setFullname(dto.getFullname());
	    user.setEmail(dto.getEmail());
	    user.setPhone(dto.getPhone());
	    user.setAddress(dto.getAddress());
	    user.setBirthday(dto.getBirthday());
	    return user;
	}

	public Users convertToEntity(ReqUpdateUserDTO dto) {
	    Users user = new Users();
	    user.setId(dto.getId());
	    user.setUsername(dto.getUsername());
	    user.setPassword(dto.getPassword());
	    user.setFullname(dto.getFullname());
	    user.setEmail(dto.getEmail());
	    user.setPhone(dto.getPhone());
	    user.setAddress(dto.getAddress());
	    user.setBirthday(dto.getBirthday());
	    return user;
	}
	
	



}
