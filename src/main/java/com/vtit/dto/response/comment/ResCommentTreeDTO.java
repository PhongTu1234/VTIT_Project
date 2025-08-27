package com.vtit.dto.response.comment;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCommentTreeDTO {
    private Integer id;
    private String content;
    private String createdBy;
    private Instant createdDate;
    private String updateBy;
    private Instant updateDate;
    private List<ResCommentTreeDTO> replies; // tree đệ quy
}
