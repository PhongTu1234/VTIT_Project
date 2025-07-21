package com.vtit.dto.request.User;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateUserDTO {
	private Integer id;
	private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private Instant birthday;
    
}
