package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.BookCategoryRepository;
import com.vtit.service.BookCategoryService;

@Service
public class BookCategoryServiceImpl implements BookCategoryService{
	private final BookCategoryRepository bookCategoryRepository;
	
	public BookCategoryServiceImpl(BookCategoryRepository bookCategoryRepository) {
		this.bookCategoryRepository = bookCategoryRepository;
	}
}
