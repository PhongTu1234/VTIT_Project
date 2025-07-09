package com.vtit.service;

import java.util.List;
import java.util.Optional;

import com.vtit.entity.Category;

public interface CategoryService {
	List<Category> findAll();
	Optional<Category> findById(String id);
	Category create(Category category);
	Category update(Category category);
    void delete(String id);

}
