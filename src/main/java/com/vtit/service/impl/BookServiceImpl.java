package com.vtit.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vtit.dto.request.book.ReqCreateBookDTO;
import com.vtit.dto.request.book.ReqUpdateBookDTO;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.User.ResUserDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResCreateBookDTO;
import com.vtit.dto.response.book.ResUpdateBookDTO;
import com.vtit.dto.response.category.ResCategoryDTO;
import com.vtit.entity.Book;
import com.vtit.entity.Category;
import com.vtit.exception.DuplicateResourceException;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.BookRepository;
import com.vtit.reponsitory.CategoryRepository;
import com.vtit.service.BookService;
import com.vtit.utils.IdValidator;

@Service
public class BookServiceImpl implements BookService {
	private final BookRepository bookRepository;
	private final RestTemplate restTemplate;
	private final CategoryRepository categoryRepository;

	public BookServiceImpl(BookRepository bookRepository, RestTemplate restTemplate,
			CategoryRepository categoryRepository) {
		this.bookRepository = bookRepository;
		this.restTemplate = restTemplate;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public boolean existsByTitleAndAuthor(String title, String author) {
		return bookRepository.existsByTitleAndAuthor(title, author);
	}

	@Override
	public void syncBooksFromGoogleApi() {
		String url = "https://www.googleapis.com/books/v1/volumes?q=java";
		Map<String, Object> response = restTemplate.getForObject(url, Map.class);

		if (response == null || response.get("items") == null) {
			System.out.println("❌ Không có dữ liệu từ Google Books API.");
			return;
		}

		List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
		int savedCount = 0;

		for (Map<String, Object> item : items) {
			Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
			Map<String, Object> imageLinks = (Map<String, Object>) volumeInfo.get("imageLinks");

			// Lấy thông tin sách
			String title = (String) volumeInfo.get("title");
			String publisher = (String) volumeInfo.get("publisher");
			String publishedDateStr = (String) volumeInfo.get("publishedDate");
			Instant publishedDate = parsePublishedDate(publishedDateStr);
			String description = (String) volumeInfo.get("description");
			String language = (String) volumeInfo.get("language");
			String printType = (String) volumeInfo.get("printType");
			Integer pageCount = volumeInfo.get("pageCount") != null ? ((Number) volumeInfo.get("pageCount")).intValue()
					: null;

			List<String> authorsList = (List<String>) volumeInfo.get("authors");
			String authors = authorsList != null ? String.join(", ", authorsList) : "Unknown";

			String thumbnailUrl = imageLinks != null ? (String) imageLinks.get("thumbnail") : null;

			// Tránh trùng sách
			if (!bookRepository.existsByTitleAndAuthor(title, authors)) {
				Book book = new Book();
				book.setTitle(title);
				book.setAuthor(authors);
				book.setPublisher(publisher);
				book.setPublishedDate(publishedDate);
				book.setPageCount(pageCount);
				book.setLanguage(language);
				book.setPrintType(printType);
				book.setDescription(description);
				book.setThumbnailUrl(thumbnailUrl);
				book.setCreatedDate(Instant.now());

				// Khởi tạo danh sách category
				book.setCategory(new ArrayList<>());

				// Xử lý danh mục
				List<String> categoryNames = (List<String>) volumeInfo.get("categories");

				if (categoryNames != null && !categoryNames.isEmpty()) {
					for (String rawCategoryName : categoryNames) {
						String cleanedCategoryName = rawCategoryName != null ? rawCategoryName.trim() : "";

						if (cleanedCategoryName.isEmpty())
							continue;

						Category category = categoryRepository.findByName(cleanedCategoryName).orElseGet(() -> {
							Category newCategory = new Category();
							newCategory.setName(cleanedCategoryName);
							newCategory.setCode(cleanedCategoryName.toLowerCase().replaceAll("[^a-z0-9]", "_"));
							newCategory.setCreatedDate(Instant.now());
							return categoryRepository.save(newCategory);
						});

						if (!book.getCategory().contains(category)) {
							book.getCategory().add(category);
						}
					}
				} else {
					// Nếu không có danh mục, gán mặc định
					String defaultName = "Unknown";
					Category defaultCategory = categoryRepository.findByName(defaultName).orElseGet(() -> {
						Category newCategory = new Category();
						newCategory.setName(defaultName);
						newCategory.setCode("unknown");
						newCategory.setCreatedDate(Instant.now());
						return categoryRepository.save(newCategory);
					});

					book.getCategory().add(defaultCategory);
				}

				bookRepository.save(book);
				savedCount++;
			}
		}

		System.out.println("✅ Đồng bộ thành công " + savedCount + " sách mới.");
	}

	private Instant parsePublishedDate(String publishedDateStr) {
		if (publishedDateStr == null || publishedDateStr.isBlank())
			return null;

		try {
			if (publishedDateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
				return Instant.parse(publishedDateStr + "T00:00:00Z");
			} else if (publishedDateStr.matches("\\d{4}-\\d{2}")) {
				return Instant.parse(publishedDateStr + "-01T00:00:00Z");
			} else if (publishedDateStr.matches("\\d{4}")) {
				return Instant.parse(publishedDateStr + "-01-01T00:00:00Z");
			}
		} catch (Exception e) {
			System.out.println("⚠️ Không thể parse ngày xuất bản: " + publishedDateStr);
		}

		return null;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Book> spec, Pageable pageable) {
		Page<Book> pageBooks = bookRepository.findAll(spec, pageable);
		
		List<ResBookDTO> bookDTOs = pageBooks.getContent().stream()
				.map(this::convertToResBookDTO)
				.collect(Collectors.toList());
		
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageBooks.getTotalPages());
		mt.setTotals((int) pageBooks.getTotalElements());

		rs.setMeta(mt);
		rs.setResult(bookDTOs);
		return rs;
	}

	@Override
	public ResBookDTO findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		Book book = bookRepository.findById(idInt)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sách với id = " + idInt));
		return convertToResBookDTO(book);
	}

	@Override
	public ResCreateBookDTO create(ReqCreateBookDTO dto) {
	    List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());

	    if (categories.size() != dto.getCategoryIds().size()) {
	        List<Integer> foundIds = categories.stream()
	                                           .map(Category::getId)
	                                           .toList();
	        List<Integer> missingIds = dto.getCategoryIds().stream()
	                                      .filter(id -> !foundIds.contains(id))
	                                      .toList();
	        throw new ResourceNotFoundException("Category not found with ID(s): " + missingIds);
	    }

	    Book newBook = convertToEntity(dto);
	    newBook.setCategory(categories);
		if (bookRepository.existsByTitleAndAuthorAndPublisherAndPublishedDate(dto.getTitle(), dto.getAuthor(), dto.getPublisher(), dto.getPublishedDate())) {
			throw new DuplicateResourceException("Sách '" + dto.getTitle() + 
					" của tác giả " + dto.getAuthor() + 
					" với nhà xuất bản " + dto.getPublisher() + 
					" vào ngày " + dto.getPublishedDate() + "' đã tồn tại");
		}

	    Book savedBook = bookRepository.save(newBook);

	    return convertToResCreateBookDTO(savedBook);
	}

	@Override
	public ResUpdateBookDTO update(ReqUpdateBookDTO dto) {
	    // Kiểm tra book tồn tại
	    Book existingBook = bookRepository.findById(dto.getId())
	        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getId()));

	    // Kiểm tra danh mục (category) có tồn tại không
	    List<Category> categories = categoryRepository.findAllById(dto.getCategoryIds());
	    if (categories.size() != dto.getCategoryIds().size()) {
	        List<Integer> foundIds = categories.stream()
	                                           .map(Category::getId)
	                                           .toList();
	        List<Integer> missingIds = dto.getCategoryIds().stream()
	                                      .filter(id -> !foundIds.contains(id))
	                                      .toList();
	        throw new ResourceNotFoundException("Category not found with ID(s): " + missingIds);
	    }

	    // Cập nhật thông tin
	    Book updatedBook = convertToEntity(dto);
	    Book savedBook = bookRepository.save(updatedBook);

	    return convertToResUpdateBookDTO(savedBook);
	}


	@Override
	public void delete(String id) {
		Integer intId = IdValidator.validateAndParse(id);
		Book book = bookRepository.findById(intId)
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy danh mục với id = " + intId));
		bookRepository.delete(book);
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
	
	private ResUpdateBookDTO convertToResUpdateBookDTO(Book book) {
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
	
	private Book convertToEntity(ReqUpdateBookDTO dto) {
	    Book book = bookRepository.findById(dto.getId())
	        .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + dto.getId()));

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
