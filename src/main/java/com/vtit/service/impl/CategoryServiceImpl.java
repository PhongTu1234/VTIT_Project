package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.CategoryRepository;
import com.vtit.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{
	private final CategoryRepository categoryRepository;
	
	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
}
