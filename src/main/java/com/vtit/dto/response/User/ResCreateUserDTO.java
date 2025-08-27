package com.vtit.dto.response.User;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
	private long id;
	private String username;
	private String email;
	private String fullname;
	private String phone;
	private String address;
	private Instant birthday;
	private String avatar;
	private Instant createAt;
	private String createBy;
	
}
