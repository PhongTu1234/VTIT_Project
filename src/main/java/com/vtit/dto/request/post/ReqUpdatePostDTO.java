package com.vtit.dto.request.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdatePostDTO {
	 private Integer id;
    private String title;
    private String content;
    private Integer user;
}
