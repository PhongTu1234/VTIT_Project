package com.vtit.mapper;

import com.vtit.dto.request.book.ReqCreateBookDTO;
import com.vtit.dto.request.book.ReqUpdateBookDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResCreateBookDTO;
import com.vtit.dto.response.book.ResUpdateBookDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Category;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class BookMapper {

	private final CategoryRepository categoryRepository;
	
	public BookMapper(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
    
	public ResBookDTO convertToResBookDTO(Book book) {
		if (book == null)
			return null;

		ResBookDTO dto = new ResBookDTO();
		dto.setId(book.getId());
		dto.setTitle(book.getTitle());
		dto.setAuthor(book.getAuthor());
		dto.setPublisher(book.getPublisher());
		dto.setPublishedDate(book.getPublishedDate());
		dto.setPageCount(book.getPageCount());
		dto.setQuantity(book.getQuantity());
		dto.setPrintType(book.getPrintType());
		dto.setDescription(book.getDescription());
		dto.setImageLink(book.getThumbnailUrl());
		dto.setLanguage(book.getLanguage());
		dto.setCreatedBy(book.getCreatedBy());
		dto.setCreatedDate(book.getCreatedDate());
		dto.setUpdatedBy(book.getUpdatedBy());
		dto.setUpdatedDate(book.getUpdatedDate());

		List<ResCategoryDTO> categoryDTOs = null;
		if (book.getCategory() != null) {
			categoryDTOs = book.getCategory().stream().map(category -> {
				ResCategoryDTO catDTO = new ResCategoryDTO();
				catDTO.setId(category.getId());
				catDTO.setCode(category.getCode());
				catDTO.setName(category.getName());
				catDTO.setCreatedBy(category.getCreatedBy());
				catDTO.setCreatedDate(category.getCreatedDate());
				catDTO.setUpdatedBy(category.getUpdatedBy());
				catDTO.setUpdatedDate(category.getUpdatedDate());
				return catDTO;
			}).collect(Collectors.toList());
		}
		dto.setCategories(categoryDTOs);
		return dto;
	}

	public ResCreateBookDTO convertToResCreateBookDTO(Book book) {
		ResCreateBookDTO res = new ResCreateBookDTO();
		res.setId(book.getId());
		res.setTitle(book.getTitle());
		res.setAuthor(book.getAuthor());
		res.setPublisher(book.getPublisher());
		res.setPublishedDate(book.getPublishedDate());
		res.setPageCount(book.getPageCount());
		res.setQuantity(book.getQuantity());
		res.setPrintType(book.getPrintType());
		res.setDescription(book.getDescription());
		res.setImageLink(book.getThumbnailUrl());
		res.setLanguage(book.getLanguage());
		res.setCreatedBy(book.getCreatedBy());
		res.setCreatedDate(book.getCreatedDate());

		// Gán danh sách category vào DTO trả về nếu cần
		List<ResCategoryDTO> categoryDTOs = null;
		if (book.getCategory() != null) {
			categoryDTOs = book.getCategory().stream().map(category -> {
				ResCategoryDTO catDTO = new ResCategoryDTO();
				catDTO.setId(category.getId());
				catDTO.setCode(category.getCode());
				catDTO.setName(category.getName());
				catDTO.setCreatedBy(category.getCreatedBy());
				catDTO.setCreatedDate(category.getCreatedDate());
				catDTO.setUpdatedBy(category.getUpdatedBy());
				catDTO.setUpdatedDate(category.getUpdatedDate());
				return catDTO;
			}).collect(Collectors.toList());
		}
		res.setCategories(categoryDTOs);
		return res;
	}
	
	public ResUpdateBookDTO convertToResUpdateBookDTO(Book book) {
	    ResUpdateBookDTO dto = new ResUpdateBookDTO();

	    dto.setId(book.getId());
	    dto.setTitle(book.getTitle());
	    dto.setAuthor(book.getAuthor());
	    dto.setPublisher(book.getPublisher());
	    dto.setPublishedDate(book.getPublishedDate());
	    dto.setPageCount(book.getPageCount());
	    dto.setQuantity(book.getQuantity());
	    dto.setPrintType(book.getPrintType());
	    dto.setDescription(book.getDescription());
	    dto.setImageLink(book.getThumbnailUrl());
	    dto.setLanguage(book.getLanguage());
	    dto.setUpdatedBy(book.getUpdatedBy());
	    dto.setUpdatedDate(book.getUpdatedDate());

	    // Map categories
	    List<ResCategoryDTO> categoryDTOs = null;
		if (book.getCategory() != null) {
			categoryDTOs = book.getCategory().stream().map(category -> {
				ResCategoryDTO catDTO = new ResCategoryDTO();
				catDTO.setId(category.getId());
				catDTO.setCode(category.getCode());
				catDTO.setName(category.getName());
				catDTO.setCreatedBy(category.getCreatedBy());
				catDTO.setCreatedDate(category.getCreatedDate());
				catDTO.setUpdatedBy(category.getUpdatedBy());
				catDTO.setUpdatedDate(category.getUpdatedDate());
				return catDTO;
			}).collect(Collectors.toList());
		}
		dto.setCategories(categoryDTOs);

	    return dto;
	}


	public Book convertToEntity(ReqCreateBookDTO dto) {
		if (dto == null)
			return null;

		Book book = new Book();
		book.setTitle(dto.getTitle());
		book.setAuthor(dto.getAuthor());
		book.setPublisher(dto.getPublisher());
		book.setPublishedDate(dto.getPublishedDate());
		book.setPageCount(dto.getPageCount());
		book.setQuantity(dto.getQuantity());
		book.setPrintType(dto.getPrintType());
		book.setDescription(dto.getDescription());
		book.setThumbnailUrl(dto.getImageLink());
		book.setLanguage(dto.getLanguage());

		if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
			List<Category> categories = dto.getCategoryIds().stream()
					.map((Integer id) -> categoryRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Category not found: " + id)))
					.collect(Collectors.toList());
			book.setCategory(categories);
		}

		return book;
	}
	
	public Book convertToEntity(ReqUpdateBookDTO dto, Book book) {

	    book.setTitle(dto.getTitle());
	    book.setAuthor(dto.getAuthor());
	    book.setPublisher(dto.getPublisher());
	    book.setPublishedDate(dto.getPublishedDate());
	    book.setPageCount(dto.getPageCount());
	    book.setQuantity(dto.getQuantity());
	    book.setPrintType(dto.getPrintType());
	    book.setDescription(dto.getDescription());
	    book.setThumbnailUrl(dto.getImageLink());
	    book.setLanguage(dto.getLanguage());
	    
	    if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
			List<Category> categories = dto.getCategoryIds().stream()
					.map((Integer id) -> categoryRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Category not found: " + id)))
					.collect(Collectors.toList());
			book.setCategory(categories);
		}
	    

	    return book;
	}
}
