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
import com.vtit.mapper.CategoryMapper;
import com.vtit.reponsitory.CategoryRepository;
import com.vtit.service.CategoryService;
import com.vtit.utils.IdValidator;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResultPaginationDTO findAll(Specification<Category> spec, Pageable pageable) {
        Page<Category> pageCategories = categoryRepository.findAll(spec, pageable);

        List<ResCategoryDTO> categoryDTOs = pageCategories.getContent().stream()
                .map(categoryMapper::toResDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCategories.getTotalPages());
        meta.setTotals((int) pageCategories.getTotalElements());

        ResultPaginationDTO rs = new ResultPaginationDTO();
        rs.setMeta(meta);
        rs.setResult(categoryDTOs);
        return rs;
    }

    @Override
    public ResCategoryDTO findById(String id) {
        Integer idInt = IdValidator.validateAndParse(id);
        Category category = categoryRepository.findById(idInt)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + idInt));
        return categoryMapper.toResDTO(category);
    }

    @Override
    public ResCreateCategoryDTO create(ReqCreateCategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new DuplicateResourceException("Name '" + dto.getName() + "' already exists");
        }
        dto.setCode(dto.getName().toLowerCase().replaceAll(" ", "_"));
        Category category = categoryMapper.toEntity(dto);
        return categoryMapper.toResCreateDTO(categoryRepository.save(category));
    }

    @Override
    public ResUpdateCategoryDTO update(ReqUpdateCategoryDTO dto) {
        Category existing = categoryRepository.findById(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + dto.getId()));

        if (dto.getName() != null &&
                categoryRepository.existsByNameAndIdNot(dto.getName(), dto.getId())) {
            throw new DuplicateResourceException("Name '" + dto.getName() + "' đã tồn tại");
        }

        dto.setCode(dto.getName().toLowerCase().replaceAll(" ", "_"));
        Category updated = categoryMapper.toEntity(existing, dto);
        return categoryMapper.toResUpdateDTO(categoryRepository.save(updated));
    }

    @Override
    public void delete(String id) {
        Integer intId = IdValidator.validateAndParse(id);
        Category category = categoryRepository.findById(intId)
                .orElseThrow(() -> new IdInvalidException("Không tìm thấy danh mục với id = " + intId));
        categoryRepository.delete(category);
    }
}

