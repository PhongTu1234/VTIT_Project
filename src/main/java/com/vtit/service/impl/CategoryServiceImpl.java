package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.category.ReqCreateCategoryDTO;
import com.vtit.dto.request.category.ReqUpdateCategoryDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.dto.response.category.ResCreateCategoryDTO;
import com.vtit.dto.response.category.ResUpdateCategoryDTO;
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
	public ResultPaginationDTO findAll(Specification<Category> spec, Pageable pageable) {
		Page<Category> pageCategories = categoryRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageCategories.getTotalPages());
		mt.setTotals((int) pageCategories.getTotalElements());
		
		rs.setMeta(mt);
		
		List<ResCategoryDTO> userDTOs = pageCategories.getContent().stream()
				.map(this::convertToResCategoryDTO)
				.collect(Collectors.toList());
		
		rs.setResult(userDTOs);
		return rs;
	}

	@Override
	public ResCategoryDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Category category = categoryRepository.findById(idInt)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + idInt));
		return convertToResCategoryDTO(category);
	}

	@Override
    public ResCreateCategoryDTO create(ReqCreateCategoryDTO dto) {
    	if (categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Name '" + dto.getName() + "' already exists");
        }
    	dto.setCode((dto.getName()).toLowerCase().replaceAll(" ", "_"));
		return convertToResCreateCategoryDTO(categoryRepository.save(convertToEntity(dto)));
    }

    @Override
    public ResUpdateCategoryDTO update(ReqUpdateCategoryDTO updatedCategory) {

        // Tìm category theo ID
    	Category existingCategory = categoryRepository.findById(updatedCategory.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + updatedCategory.getId()));

        // Kiểm tra trùng name
        if (updatedCategory.getName() != null &&
        		categoryRepository.existsByNameAndIdNot(updatedCategory.getName(), updatedCategory.getId())) {
            throw new DuplicateResourceException("Name '" + updatedCategory.getName() + "' đã tồn tại");
        }
        existingCategory.setCode((updatedCategory.getName()).toLowerCase().replaceAll(" ", "_"));

        return convertToResUpdateCategoryDTO(categoryRepository.save(convertToEntity(existingCategory, updatedCategory)));
    }

    
    @Override
    public void delete(String id) {
        Integer intId = IdValidator.validateAndParse(id);
        Category category = categoryRepository.findById(intId)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy danh mục với id = " + intId));
        categoryRepository.delete(category);
    }
    
    public ResCreateCategoryDTO convertToResCreateCategoryDTO(Category category) {
        ResCreateCategoryDTO dto = new ResCreateCategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setCreatedBy(category.getCreatedBy());
        dto.setCreatedDate(category.getCreatedDate());
        return dto;
    }
    
    public ResUpdateCategoryDTO convertToResUpdateCategoryDTO(Category category) {
        ResUpdateCategoryDTO dto = new ResUpdateCategoryDTO();
        dto.setId(category.getId());
        dto.setCode(category.getCode());
        dto.setName(category.getName());
        dto.setUpdatedBy(category.getUpdatedBy());
        dto.setUpdatedDate(category.getUpdatedDate());
        return dto;
    }
    
    public ResCategoryDTO convertToResCategoryDTO(Category category) {
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

    public Category convertToEntity(ReqCreateCategoryDTO dto) {
	    Category category = new Category();
	    category.setCode(dto.getCode());
	    category.setName(dto.getName());
	    return category;
	}
	
	public Category convertToEntity(Category existingCategory, ReqUpdateCategoryDTO dto) {
	    existingCategory.setCode(dto.getCode());
	    existingCategory.setName(dto.getName());
	    return existingCategory;
	}

}
