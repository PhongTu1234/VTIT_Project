package com.vtit.dto.response.post;

import java.time.Instant;

import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResPostDTO {
	private Integer id;
    private String title;
    private String content;
    private ResUserSummartDTO user;
    private String createdBy;
    private Instant createdDate;
    private String updatedBy;
    private Instant updatedDate;
    private ReactionSummaryDTO likeOrDislike;
}