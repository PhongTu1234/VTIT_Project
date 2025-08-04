package com.vtit.service.impl;

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
import com.vtit.mapper.UserMapper;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.FileService;
import com.vtit.service.UserService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final SecurityUtil securityUtil;
	private final FileService fileService;
	private final UserMapper userMapper;

	public UserServiceImpl(UserRepository userRepository,
	                       PasswordEncoder passwordEncoder,
	                       SecurityUtil securityUtil,
	                       FileService fileService,
	                       UserMapper userMapper) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.securityUtil = securityUtil;
		this.fileService = fileService;
		this.userMapper = userMapper;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Users> spec, Pageable pageable) {
		Page<Users> pageUser = userRepository.findAll(spec, pageable);
		List<ResUserDTO> userDTOs = pageUser.getContent().stream()
				.map(userMapper::convertToResUserDTO)
				.collect(Collectors.toList());

		ResultPaginationDTO.Meta meta = createPaginationMeta(pageUser, pageable);

		ResultPaginationDTO result = new ResultPaginationDTO();
		result.setMeta(meta);
		result.setResult(userDTOs);
		return result;
	}

	private ResultPaginationDTO.Meta createPaginationMeta(Page<?> page, Pageable pageable) {
		ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
		meta.setPage(pageable.getPageNumber() + 1);
		meta.setPageSize(pageable.getPageSize());
		meta.setPages(page.getTotalPages());
		meta.setTotals((int) page.getTotalElements());
		return meta;
	}

	@Override
	public ResUserDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Users user = userRepository.findById(idInt)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + idInt));
		return userMapper.convertToResUserDTO(user);
	}

	@Override
	public ResCreateUserDTO create(ReqCreateUserDTO dto, MultipartFile avatar) throws Exception {
		validateDuplicate(dto.getUsername(), dto.getEmail(), dto.getPhone());

		dto.setPassword(passwordEncoder.encode(dto.getPassword()));
		Users entity = userMapper.convertToEntity(dto);

		String avatarFileName = null;
		if (avatar != null && !avatar.isEmpty()) {
			avatarFileName = fileService.generateFileName(avatar);
			entity.setAvatar(avatarFileName);
		}

		Users savedUser = userRepository.save(entity);

		try {
			if (avatarFileName != null) {
				fileService.createDirectory("avatars");
				fileService.store(avatar, "avatars", avatarFileName);
			}
			return userMapper.convertToResCreateUserDTO(savedUser);
		} catch (Exception e) {
			userRepository.deleteById(savedUser.getId());
			throw new RuntimeException("Đã xảy ra lỗi khi lưu avatar. Dữ liệu đã được rollback.", e);
		}
	}

	@Override
	public ResUpdateUserDTO update(ReqUpdateUserDTO dto, MultipartFile avatar) throws Exception {
		Users user = userRepository.findById(dto.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng với id = " + dto.getId()));

		validateDuplicate(dto.getUsername(), dto.getEmail(), dto.getPhone(), dto.getId());

		String oldAvatar = user.getAvatar();
		String newAvatarFileName = null;

		if (avatar != null && !avatar.isEmpty()) {
			String extension = "";
			String originalFilename = avatar.getOriginalFilename();
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}
			newAvatarFileName = UUID.randomUUID().toString() + extension;
			user.setAvatar(newAvatarFileName);
		}

		String encodedPassword = StringUtils.hasText(dto.getPassword())
				? passwordEncoder.encode(dto.getPassword())
				: null;

		userMapper.updateEntityFromDTO(user, dto, encodedPassword, newAvatarFileName);

		try {
			userRepository.save(user);

			if (newAvatarFileName != null) {
				fileService.createDirectory("avatars");
				fileService.store(avatar, "avatars", newAvatarFileName);
			}

			if (newAvatarFileName != null && oldAvatar != null && !oldAvatar.equals(newAvatarFileName)) {
				fileService.deleteFile("avatars", oldAvatar);
			}

			return userMapper.convertToResUpdateUserDTO(user);

		} catch (Exception e) {
			if (newAvatarFileName != null) {
				fileService.deleteFile("avatars", newAvatarFileName);
				user.setAvatar(oldAvatar);
				userRepository.save(user);
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

	private void validateDuplicate(String username, String email, String phone) {
		if (userRepository.existsByUsername(username)) {
			throw new DuplicateResourceException("Username '" + username + "' đã tồn tại");
		}
		if (email != null && userRepository.existsByEmail(email)) {
			throw new DuplicateResourceException("Email '" + email + "' đã tồn tại");
		}
		if (phone != null && userRepository.existsByPhone(phone)) {
			throw new DuplicateResourceException("Phone '" + phone + "' đã tồn tại");
		}
	}

	private void validateDuplicate(String username, String email, String phone, Integer id) {
		if (username != null && userRepository.existsByUsernameAndIdNot(username, id)) {
			throw new DuplicateResourceException("Username '" + username + "' đã tồn tại");
		}
		if (email != null && userRepository.existsByEmailAndIdNot(email, id)) {
			throw new DuplicateResourceException("Email '" + email + "' đã tồn tại");
		}
		if (StringUtils.hasText(phone) && userRepository.existsByPhoneAndIdNot(phone, id)) {
			throw new DuplicateResourceException("Phone '" + phone + "' đã tồn tại");
		}
	}
}
