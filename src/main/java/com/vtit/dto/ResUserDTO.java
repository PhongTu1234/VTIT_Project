package com.vtit.dto;

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
	private Instant createAt;
	private Instant updateAt;
}
