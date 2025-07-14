package com.vtit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {
	private String accessToken;
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

}
