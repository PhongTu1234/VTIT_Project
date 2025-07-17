package com.vtit.dto.response.postReaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionSummaryDTO {
    private long likeCount;
    private long dislikeCount;
}

