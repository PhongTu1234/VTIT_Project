package com.vtit.dto.response.category;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateCategoryDTO {
    private Integer id;
    private String code;
    private String name;
    private String createdBy;
    private Instant createdDate;
}
