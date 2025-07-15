package com.vtit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("user_info")
	private UserInfo userInfo;
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserInfo {
		private long id;
		private String username;
		private String email;
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class UserGetAccount {
		private UserInfo userInfo;
	}

}
