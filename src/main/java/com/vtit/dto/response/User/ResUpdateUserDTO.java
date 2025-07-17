package com.vtit.dto.response.User;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateUserDTO {
	private long id;
	private String username;
	private String fullname;
	private String phone;
	private String email;
	private String address;
	private Instant birthday;
	private Instant updatedAt;
	
}
