package com.vtit.dto.response.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResTopPostDTO {
    private Integer postId;
    private String title;
    private Long likeCount;
}
