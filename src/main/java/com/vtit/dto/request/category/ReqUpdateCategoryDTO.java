package com.vtit.dto.request.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateCategoryDTO {

    @NotNull(message = "ID is required")
    private Integer id;

    private String code;

    private String name;
}
