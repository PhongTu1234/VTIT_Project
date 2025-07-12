package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vtit.dto.Meta;
import com.vtit.dto.ResultPaginationDTO;
import com.vtit.entity.Category;
import com.vtit.entity.Users;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.CategoryRepository;
import com.vtit.service.CategoryService;
import com.vtit.utils.IdValidator;

@Service
public class CategoryServiceImpl implements CategoryService{
	private final CategoryRepository categoryRepository;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public ResultPaginationDTO findAll(Pageable pageable) {
		Page<Category> pageCategories = categoryRepository.findAll(pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		Meta mt = new Meta();
		
		mt.setPage(pageCategories.getNumber() + 1);
		mt.setPageSize(pageCategories.getSize());
		mt.setPages(pageCategories.getTotalPages());
		mt.setTotals((int) pageCategories.getTotalElements());
		
		rs.setMeta(mt);
		rs.setResult(pageCategories.getContent());
		return rs;
	}

	@Override
	public Optional<Category> findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		return categoryRepository.findById(idInt);
	}

	@Override
    public Category create(Category category) {
    	if (categoryRepository.existsByName(category.getName())) {
            throw new DuplicateResourceException("Name '" + category.getName() + "' already exists");
        }
    	category.setCode((category.getName()).toLowerCase().replaceAll(" ", "_"));
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category updatedCategory) {

        // Tìm category theo ID
    	Category existingCategory = categoryRepository.findById(updatedCategory.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + updatedCategory.getId()));

        // Kiểm tra trùng name
        if (updatedCategory.getName() != null &&
        		categoryRepository.existsByNameAndIdNot(updatedCategory.getName(), updatedCategory.getId())) {
            throw new DuplicateResourceException("Name '" + updatedCategory.getName() + "' đã tồn tại");
        }
        existingCategory.setCode((updatedCategory.getName()).toLowerCase().replaceAll(" ", "_"));

        // Cập nhật thông tin (nếu có truyền lên)
        if (updatedCategory.getName() != null) existingCategory.setName(updatedCategory.getName());
        if (updatedCategory.getCode() != null) existingCategory.setCode(updatedCategory.getCode());

        return categoryRepository.save(existingCategory);
    }

    
    @Override
    public void delete(String id) {
        Integer intId = IdValidator.validateAndParse(id);
        Category category = categoryRepository.findById(intId)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy danh mục với id = " + intId));
        categoryRepository.delete(category);
    }
}
