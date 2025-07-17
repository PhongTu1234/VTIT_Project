package com.vtit.dto.response.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
	private Integer id;
	private String title;
    private String content;
    private Boolean isActive;
}
