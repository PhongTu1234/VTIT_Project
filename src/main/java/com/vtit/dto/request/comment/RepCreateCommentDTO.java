package com.vtit.dto.request.comment;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepCreateCommentDTO {
    private String content;
    private Integer postId;
    private Integer userId;
   
}