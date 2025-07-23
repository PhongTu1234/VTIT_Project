package com.vtit.dto.response.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPostReactionDTO {
	private Integer id;
	private String content;
	private String authorName;
	private String reactionType;
}
