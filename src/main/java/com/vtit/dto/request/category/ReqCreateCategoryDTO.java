package com.vtit.dto.request.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateCategoryDTO {
    
    @NotBlank(message = "Code is required")
    private String code;

    private String name;
}