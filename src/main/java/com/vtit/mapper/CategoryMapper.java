package com.vtit.mapper;

import org.springframework.stereotype.Component;

import com.vtit.dto.request.category.ReqCreateCategoryDTO;
import com.vtit.dto.request.category.ReqUpdateCategoryDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.dto.response.category.ResCreateCategoryDTO;
import com.vtit.dto.response.category.ResUpdateCategoryDTO;
import com.vtit.entity.Category;

@Component
public class CategoryMapper {

    public ResCreateCategoryDTO toResCreateDTO(Category category) {
        ResCreateCategoryDTO dto = new ResCreateCategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setCreatedDate(category.getCreatedDate());
        return dto;
    }

    public ResUpdateCategoryDTO toResUpdateDTO(Category category) {
        ResUpdateCategoryDTO dto = new ResUpdateCategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setUpdatedDate(category.getUpdatedDate());
        return dto;
    }

    public ResCategoryDTO toResDTO(Category category) {
        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setCreatedDate(category.getCreatedDate());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setUpdatedDate(category.getUpdatedDate());
        return dto;
    }

    public Category toEntity(ReqCreateCategoryDTO dto) {
        Category category = new Category();
        category.setCode(dto.getCode());
        category.setName(dto.getName());
        return category;
    }

    public Category toEntity(Category existingCategory, ReqUpdateCategoryDTO dto) {
        existingCategory.setCode(dto.getCode());
        existingCategory.setName(dto.getName());
        return existingCategory;
    }
}
