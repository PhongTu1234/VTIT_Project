package com.vtit.dto.response.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResCategoryBookStatisticDTO {
    private String categoryName;
    private Long bookCount;
}
