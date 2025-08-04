package com.vtit.dto.response.comment;

import java.time.Instant;

import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateCommentDTO {
	private Integer id;
    private String content;
    private String createdBy;
    private Instant createdDate;
    
    private ResPostSummaryDTO Post;
    private Integer parentId;

//    private ResUserSummartDTO User;
}
