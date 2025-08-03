package com.vtit.service.impl;

import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
import com.vtit.service.FileService;
import com.vtit.service.UserService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

@Service
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SecurityUtil securityUtil;
	private final FileService fileService;

	public UserServiceImpl
	(
		UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		SecurityUtil securityUtil,
		FileService fileService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.securityUtil = securityUtil;
		this.fileService = fileService;
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
	public ResCreateUserDTO create(ReqCreateUserDTO user, MultipartFile avatar) throws URISyntaxException, Exception {
	    // 1. Validate trùng lặp
	    if (userRepository.existsByUsername(user.getUsername())) {
	        throw new DuplicateResourceException("Username '" + user.getUsername() + "' đã tồn tại");
	    }
	    if (user.getEmail() != null && userRepository.existsByEmail(user.getEmail())) {
	        throw new DuplicateResourceException("Email '" + user.getEmail() + "' đã tồn tại");
	    }
	    if (user.getPhone() != null && userRepository.existsByPhone(user.getPhone())) {
	        throw new DuplicateResourceException("Phone '" + user.getPhone() + "' đã tồn tại");
	    }

	    String avatarFileName = null;

	    try {
	        // 2. Lưu file nếu có
	        if (avatar != null && !avatar.isEmpty()) {
	            fileService.createDirectory("avatars"); // Tạo thư mục nếu chưa có
	            avatarFileName = fileService.store(avatar, "avatars"); // Lưu và lấy tên file
	        }

	        // 3. Mã hóa mật khẩu
	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        // 4. Chuyển DTO thành entity
	        Users entity = convertToEntity(user);
	        entity.setAvatar(avatarFileName); // Gán tên file avatar

	        // 5. Lưu DB
	        Users userDB = userRepository.save(entity);

	        // 6. Trả DTO phản hồi
	        return convertToResCreateUserDTO(userDB);
	    } catch (Exception e) {
	        // Nếu lỗi và file đã lưu thì rollback file
	        if (avatarFileName != null) {
	            fileService.deleteFile("avatars", avatarFileName);
	        }
	        throw e; // Ném lại exception
	    }
	}


	@Override
	public ResUpdateUserDTO update(ReqUpdateUserDTO dto, MultipartFile avatar) throws URISyntaxException, Exception {
	    Users user = userRepository.findById(dto.getId())
	        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + dto.getId()));

	    // Kiểm tra trùng username/email/phone
	    if (dto.getUsername() != null && userRepository.existsByUsernameAndIdNot(dto.getUsername(), dto.getId())) {
	        throw new DuplicateResourceException("Username '" + dto.getUsername() + "' đã tồn tại");
	    }
	    if (dto.getEmail() != null && userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
	        throw new DuplicateResourceException("Email '" + dto.getEmail() + "' đã tồn tại");
	    }
	    if (StringUtils.hasText(dto.getPhone()) && userRepository.existsByPhoneAndIdNot(dto.getPhone(), dto.getId())) {
	        throw new DuplicateResourceException("Phone '" + dto.getPhone() + "' đã tồn tại");
	    }

	    String oldAvatar = user.getAvatar();
	    String newAvatar = null;

	    try {
	        // Nếu có file mới => lưu và cập nhật tên
	        if (avatar != null && !avatar.isEmpty()) {
	            fileService.createDirectory("avatars");
	            newAvatar = fileService.store(avatar, "avatars");
	            user.setAvatar(newAvatar);
	        }

	        // Cập nhật các thông tin khác
	        if (StringUtils.hasText(dto.getUsername())) user.setUsername(dto.getUsername());
	        if (StringUtils.hasText(dto.getFullname())) user.setFullname(dto.getFullname());
	        if (StringUtils.hasText(dto.getEmail())) user.setEmail(dto.getEmail());
	        if (StringUtils.hasText(dto.getPhone())) user.setPhone(dto.getPhone());
	        if (StringUtils.hasText(dto.getAddress())) user.setAddress(dto.getAddress());
	        if (dto.getBirthday() != null) user.setBirthday(dto.getBirthday());
	        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
	            user.setPassword(passwordEncoder.encode(dto.getPassword()));
	        }

	        // Lưu DB
	        userRepository.save(user);

	        // Nếu cập nhật avatar thành công => xóa avatar cũ
	        if (newAvatar != null && oldAvatar != null && !oldAvatar.equals(newAvatar)) {
	            fileService.deleteFile("avatars", oldAvatar);
	        }

	        return convertToResUpdateUserDTO(user);
	    } catch (Exception e) {
	        // Nếu có lỗi sau khi đã lưu file => rollback file mới
	        if (newAvatar != null) {
	            fileService.deleteFile("avatars", newAvatar);
	        }
	        throw e;
	    }
	}

	@Override
	public void delete(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Users user = userRepository.findById(idInt)
			.orElseThrow(() -> new IdInvalidException("Không tìm thấy người dùng với id = " + idInt));
		userRepository.delete(user);
	}

	@Override
	public Users handleGetUserByUsername(String email) {
		return userRepository.findByEmail(email);
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
		
		String avatarUrl = ServletUriComponentsBuilder
			    .fromCurrentContextPath()
			    .path("/storage/avatars/")
			    .path(user.getAvatar())
			    .toUriString();
		dto.setAvatar(avatarUrl);
		
		dto.setCreateAt(user.getCreatedDate());
		dto.setCreateBy(user.getCreatedBy());
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
		String avatarUrl = ServletUriComponentsBuilder
			    .fromCurrentContextPath()
			    .path("/storage/avatars/")
			    .path(user.getAvatar())
			    .toUriString();
		dto.setAvatar(avatarUrl);
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
		String avatarUrl = ServletUriComponentsBuilder
			    .fromCurrentContextPath()
			    .path("/storage/avatars/")
			    .path(user.getAvatar())
			    .toUriString();
		dto.setAvatar(avatarUrl);
		dto.setUpdatedAt(user.getUpdatedDate());
		dto.setUpdateBy(user.getUpdatedBy());
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
	    String avatarUrl = ServletUriComponentsBuilder
			    .fromCurrentContextPath()
			    .path("/storage/avatars/")
			    .path(user.getAvatar())
			    .toUriString();
		dto.setAvatar(avatarUrl);
	    user.setRefreshToken(null);
	    return user;
	}
}
