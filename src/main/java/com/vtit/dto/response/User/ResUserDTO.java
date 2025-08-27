package com.vtit.dto.response.User;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
	private long id;
	private String username;
	private String email;
	private String fullname;
	private String phone;
	private String address;
	private Instant birthday;
	private String avatar;
	private String createdBy;
	private Instant createAt;
	private String updatedBy;
	private Instant updateAt;
}
