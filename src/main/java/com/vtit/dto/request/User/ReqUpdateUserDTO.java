package com.vtit.dto.request.User;

import java.time.Instant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateUserDTO {
	private Integer id;
	
	private String username;
    private String password;
    private String fullname;
    
    @Email(message = "Email không hợp lệ")
    private String email;
    
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    @Pattern(regexp = "^[0-9+()\\-\\s]*$", message = "Số điện thoại không hợp lệ")
    private String phone;
    private String address;
    private String avatar;
    private Instant birthday;
    
}
