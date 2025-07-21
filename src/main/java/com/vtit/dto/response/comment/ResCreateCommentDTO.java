package com.vtit.dto.response.comment;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateCommentDTO {
	private Integer id;
    private String content;
    private Integer postId;
    private Integer userId;
    private String createdBy;
    private Instant createdDate;
}
