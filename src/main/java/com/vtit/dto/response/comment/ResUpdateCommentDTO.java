package com.vtit.dto.response.comment;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCommentDTO {
	private Integer id;
    private String content;
    private Integer postId;
    private Integer userId;
    private String updatedBy;
    private Instant updatedDate;
}
