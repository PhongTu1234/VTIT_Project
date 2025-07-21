package com.vtit.dto.response.post;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdatePostDTO {
	 private Integer id;
    private String title;
    private String content;
    private Integer user;
    private String updatedBy;
    private Instant updatedDate;
}
