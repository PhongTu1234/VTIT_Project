package com.vtit.dto.response.comment;

import java.time.Instant;

import com.vtit.dto.response.post.ResPostSummaryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCommentDTO {
	private Integer id;
    private String content;
    private String updatedBy;
    private Instant updatedDate;
    
    private ResPostSummaryDTO Post;
}
