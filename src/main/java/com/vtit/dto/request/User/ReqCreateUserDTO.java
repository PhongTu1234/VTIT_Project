package com.vtit.dto.request.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vtit.dto.request.role.ReqRoleIdDTO;

@Getter
@Setter
public class ReqCreateUserDTO {

    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Fullname không được để trống")
    private String fullname;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String phone;

    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant birthday;

    private ReqRoleIdDTO role;
}
