package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.category.ReqCreateCategoryDTO;
import com.vtit.dto.request.category.ReqUpdateCategoryDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.dto.response.category.ResCreateCategoryDTO;
import com.vtit.dto.response.category.ResUpdateCategoryDTO;
import com.vtit.entity.Category;

public interface CategoryService {
	ResultPaginationDTO findAll(Specification<Category> spec, Pageable pageable);
	ResCategoryDTO findById(String id);
	ResUpdateCategoryDTO update(ReqUpdateCategoryDTO dto);
    void delete(String id);
	ResCreateCategoryDTO create(ReqCreateCategoryDTO dto);

}
