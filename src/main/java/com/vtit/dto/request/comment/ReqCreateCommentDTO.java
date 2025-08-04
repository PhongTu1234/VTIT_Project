package com.vtit.dto.request.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateCommentDTO {
    private String content;
    private Integer postId;
    private Integer parentId;
   
}