package com.vtit.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Category;

public interface CategoryService {
	ResultPaginationDTO findAll(Specification<Category> spec, Pageable pageable);
	Optional<Category> findById(String id);
	Category create(Category category);
	Category update(Category category);
    void delete(String id);

}
