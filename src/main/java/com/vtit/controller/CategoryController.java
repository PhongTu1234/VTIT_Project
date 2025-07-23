package com.vtit.controller;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.category.ReqCreateCategoryDTO;
import com.vtit.dto.request.category.ReqUpdateCategoryDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.dto.response.category.ResCreateCategoryDTO;
import com.vtit.dto.response.category.ResUpdateCategoryDTO;
import com.vtit.entity.Category;
import com.vtit.service.CategoryService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/library/category")
public class CategoryController {
	
	private final CategoryService categoryService;
	
	public CategoryController(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@GetMapping
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Category> spec, Pageable pageable){
		return ResponseEntity.ok(categoryService.findAll(spec, pageable));
	}
	
	@GetMapping("/{id}")
    public ResponseEntity<ResCategoryDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }
	
	@PostMapping
    public ResponseEntity<ResCreateCategoryDTO> create(@Valid @RequestBody ReqCreateCategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(categoryService.create(dto));
    }
	
	@PutMapping()
    public ResponseEntity<ResUpdateCategoryDTO> update(@Valid @RequestBody ReqUpdateCategoryDTO dto) {
        return ResponseEntity.ok(categoryService.update(dto));
    }
    
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
    	categoryService.delete(id);
    }

}
