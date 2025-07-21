package com.vtit.dto.response.post;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreatePostDTO {
	private Integer id;
    private String title;
    private String content;
    private Integer user;
    private String createdBy;
    private Instant createdDate;
}