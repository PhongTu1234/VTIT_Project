package com.vtit.mapper;

import com.vtit.dto.request.User.ReqCreateUserDTO;
import com.vtit.dto.request.User.ReqUpdateUserDTO;
import com.vtit.dto.response.User.ResCreateUserDTO;
import com.vtit.dto.response.User.ResUpdateUserDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.entity.Users;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Component
public class UserMapper {

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
        user.setRefreshToken(null); // hoặc không cần nếu mặc định đã là null
        return user;
    }
    
    public void updateEntityFromDTO(Users user, ReqUpdateUserDTO dto, String encodedPasswordIfAny, String newAvatarFileName) {
        if (StringUtils.hasText(dto.getUsername())) {
            user.setUsername(dto.getUsername());
        }
        if (StringUtils.hasText(dto.getFullname())) {
            user.setFullname(dto.getFullname());
        }
        if (StringUtils.hasText(dto.getEmail())) {
            user.setEmail(dto.getEmail());
        }
        if (StringUtils.hasText(dto.getPhone())) {
            user.setPhone(dto.getPhone());
        }
        if (StringUtils.hasText(dto.getAddress())) {
            user.setAddress(dto.getAddress());
        }
        if (dto.getBirthday() != null) {
            user.setBirthday(dto.getBirthday());
        }
        if (StringUtils.hasText(encodedPasswordIfAny)) {
            user.setPassword(encodedPasswordIfAny);
        }
        if (StringUtils.hasText(newAvatarFileName)) {
            user.setAvatar(newAvatarFileName);
        }
    }

}
