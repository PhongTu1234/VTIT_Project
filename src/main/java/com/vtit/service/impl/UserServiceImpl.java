package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vtit.dto.Meta;
import com.vtit.dto.ResCreateUserDTO;
import com.vtit.dto.ResUpdateUserDTO;
import com.vtit.dto.ResUserDTO;
import com.vtit.dto.ResultPaginationDTO;
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
		Page<Users> pageUser = this.userRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		Meta mt = new Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageUser.getTotalPages());
		mt.setTotals((int) pageUser.getTotalElements());

		rs.setMeta(mt);
		
		List<ResUserDTO> resUserDTO = pageUser.getContent()
				.stream().map(item -> new ResUserDTO(
						item.getId(),
						item.getUsername(),
						item.getEmail(),
						item.getFullname(),
						item.getPhone(),
						item.getAddress(),
						item.getBirthday(),
						item.getCreatedDate(),
						item.getUpdatedDate()))
				.collect(Collectors.toList());
		
		rs.setResult(resUserDTO);
		return rs;
	}

	@Override
	public ResUserDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Users existingUser = userRepository.findById(idInt)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + idInt));
		return convertToResUserDTO(existingUser);
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
		userRepository.save(user);

		return userRepository.save(user);
	}

	@Override
	public ResUpdateUserDTO update(String id, Users updatedUser) {
		Integer intId = IdValidator.validateAndParse(id);

		// Tìm user theo ID
		Users existingUser = userRepository.findById(intId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + intId));

		// Kiểm tra trùng username
		if (updatedUser.getUsername() != null
				&& userRepository.existsByUsernameAndIdNot(updatedUser.getUsername(), intId)) {
			throw new DuplicateResourceException("Username '" + updatedUser.getUsername() + "' đã tồn tại");
		}

		// Kiểm tra trùng email
		if (updatedUser.getEmail() != null && userRepository.existsByEmailAndIdNot(updatedUser.getEmail(), intId)) {
			throw new DuplicateResourceException("Email '" + updatedUser.getEmail() + "' đã tồn tại");
		}

		// Kiểm tra trùng phone
		if (updatedUser.getPhone() != null && userRepository.existsByPhoneAndIdNot(updatedUser.getPhone(), intId)) {
			throw new DuplicateResourceException("Phone '" + updatedUser.getPhone() + "' đã tồn tại");
		}

		// Hash lại mật khẩu nếu có
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
			String hashPassword = this.passwordEncoder.encode(updatedUser.getPassword());
			existingUser.setPassword(hashPassword);
		}

		// Cập nhật thông tin (nếu có truyền lên)
		if (updatedUser.getUsername() != null)
			existingUser.setUsername(updatedUser.getUsername());
		if (updatedUser.getFullname() != null)
			existingUser.setFullname(updatedUser.getFullname());
		if (updatedUser.getEmail() != null)
			existingUser.setEmail(updatedUser.getEmail());
		if (updatedUser.getPhone() != null)
			existingUser.setPhone(updatedUser.getPhone());
		if (updatedUser.getAddress() != null)
			existingUser.setAddress(updatedUser.getAddress());
		if (updatedUser.getBirthday() != null)
			existingUser.setBirthday(updatedUser.getBirthday());
		if (updatedUser.getUpdatedBy() != null)
			existingUser.setUpdatedBy(updatedUser.getUpdatedBy());
		userRepository.save(existingUser);
		return convertToResUpdateUserDTO(existingUser);
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
		return this.userRepository.findByEmail(username);
	}

	@Override
	public ResCreateUserDTO convertToResCreateUserDTO(Users user) {
		ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
		resCreateUserDTO.setId(user.getId());
		resCreateUserDTO.setUsername(user.getUsername());
		resCreateUserDTO.setEmail(user.getEmail());
		resCreateUserDTO.setFullname(user.getFullname());
		resCreateUserDTO.setPhone(user.getPhone());
		resCreateUserDTO.setAddress(user.getAddress());
		resCreateUserDTO.setBirthday(user.getBirthday());
		resCreateUserDTO.setCreateAt(user.getCreatedDate().toString());
		return resCreateUserDTO;
	}

	@Override
	public ResUserDTO convertToResUserDTO(Users user) {
		ResUserDTO resUserDTO = new ResUserDTO();
		resUserDTO.setId(user.getId());
		resUserDTO.setUsername(user.getUsername());
		resUserDTO.setEmail(user.getEmail());
		resUserDTO.setFullname(user.getFullname());
		resUserDTO.setPhone(user.getPhone());
		resUserDTO.setAddress(user.getAddress());
		resUserDTO.setBirthday(user.getBirthday());
		resUserDTO.setCreateAt(user.getCreatedDate());
		resUserDTO.setUpdateAt(user.getUpdatedDate() != null ? user.getUpdatedDate() : null);
		return resUserDTO;
	}

	@Override
	public ResUpdateUserDTO convertToResUpdateUserDTO(Users user) {
		ResUpdateUserDTO resUpdateUserDTO = new ResUpdateUserDTO();
		resUpdateUserDTO.setId(user.getId());
		resUpdateUserDTO.setUsername(user.getUsername());
		resUpdateUserDTO.setFullname(user.getFullname());
		resUpdateUserDTO.setEmail(user.getEmail() != null ? user.getEmail() : null);
		resUpdateUserDTO.setPhone(user.getPhone() != null ? user.getPhone() : null);
		resUpdateUserDTO.setBirthday(user.getBirthday() != null ? user.getBirthday() : null);
		resUpdateUserDTO.setAddress(user.getAddress() != null ? user.getAddress() : null);
		resUpdateUserDTO.setUpdatedAt(user.getUpdatedDate() != null ? user.getUpdatedDate() : null);
		return resUpdateUserDTO;
	}

	@Override
	public void updateUserToken(String email, String token) {
		Users user = this.handleGetUserByUsername(email);
		if (user != null) {
			user.setRefreshToken(token);
			this.userRepository.save(user);
		}
		
	}

	@Override
	public Users handleGetUserByUsernames(String email) {
		// TODO Auto-generated method stub
		return this.userRepository.findByUsername(email);
	}

	@Override
	public Users findByRefreshTokenAndEmail(String refreshToken, String email) {
		// TODO Auto-generated method stub
		return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
	}
}
