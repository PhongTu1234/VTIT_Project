package com.vtit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.entity.Book;
import com.vtit.entity.BookCategory;
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

	public BookServiceImpl(BookRepository bookRepository, RestTemplate restTemplate, CategoryRepository categoryRepository) {
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
	    Map response = restTemplate.getForObject(url, Map.class);

	    if (response == null || response.get("items") == null) {
	        System.out.println("❌ Không có dữ liệu từ Google Books API.");
	        return;
	    }

	    List<Map<String, Object>> items = (List<Map<String, Object>>) response.get("items");
	    int savedCount = 0;

	    for (Map<String, Object> item : items) {
	        Map<String, Object> volumeInfo = (Map<String, Object>) item.get("volumeInfo");
	        Map<String, Object> imageLinks = (Map<String, Object>) volumeInfo.get("imageLinks");

	        // Lấy thông tin cơ bản
	        String title = (String) volumeInfo.get("title");
	        String publisher = (String) volumeInfo.get("publisher");
	        String publishedDateStr = (String) volumeInfo.get("publishedDate");
	        Instant publishedDate = parsePublishedDate(publishedDateStr);
	        String description = (String) volumeInfo.get("description");
	        String language = (String) volumeInfo.get("language");
	        String printType = (String) volumeInfo.get("printType");
	        Integer pageCount = volumeInfo.get("pageCount") != null
	                ? ((Number) volumeInfo.get("pageCount")).intValue()
	                : null;

	        List<String> authorsList = (List<String>) volumeInfo.get("authors");
	        String authors = authorsList != null ? String.join(", ", authorsList) : "Unknown";

	        String thumbnailUrl = imageLinks != null ? (String) imageLinks.get("thumbnail") : null;

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

	            if (book.getBookCategories() == null) {
	                book.setBookCategories(new ArrayList<>());
	            }

	            List<String> categoryNames = (List<String>) volumeInfo.get("categories");
	            if (categoryNames != null) {
	                for (String categoryName : categoryNames) {
	                    Category category = categoryRepository.findByName(categoryName)
	                            .orElseGet(() -> {
	                                Category newCat = new Category();
	                                newCat.setName(categoryName);
	                                newCat.setCode(categoryName.toLowerCase().replaceAll(" ", "_"));
	                                newCat.setCreatedDate(Instant.now());
	                                return categoryRepository.save(newCat);
	                            });

	                    BookCategory bookCategory = new BookCategory();
	                    bookCategory.setBook(book);
	                    bookCategory.setCategory(category);

	                    book.getBookCategories().add(bookCategory);
	                }
	            }

	            bookRepository.save(book); // Tự động lưu BookCategory nếu cascade đúng
	            savedCount++;
	        }
	    }

	    System.out.println("✅ Đồng bộ thành công " + savedCount + " sách mới.");
	}



	private Instant parsePublishedDate(String dateStr) {
	    if (dateStr == null || dateStr.isEmpty()) return null;
	    try {
	        SimpleDateFormat sdf;
	        if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
	            sdf = new SimpleDateFormat("yyyy-MM-dd");
	        } else if (dateStr.matches("\\d{4}-\\d{2}")) {
	            sdf = new SimpleDateFormat("yyyy-MM");
	        } else if (dateStr.matches("\\d{4}")) {
	            sdf = new SimpleDateFormat("yyyy");
	        } else {
	            return null; // Không khớp định dạng nào
	        }
	        sdf.setLenient(false); // Không cho phép ngày không hợp lệ như 2023-13-99
	        Date parsedDate = sdf.parse(dateStr);
	        return parsedDate.toInstant(); // Chuyển Date sang Instant
	    } catch (ParseException e) {
	        e.printStackTrace();
	    }
	    return null;
	}

	@Override
	public ResultPaginationDTO findAll(Specification<Book> spec, Pageable pageable) {
		Page<Book> pageBooks = bookRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageBooks.getTotalPages());
		mt.setTotals((int) pageBooks.getTotalElements());
		
		rs.setMeta(mt);
		rs.setResult(pageBooks.getContent());
		return rs;
	}

	@Override
	public Optional<Book> findById(String id) {
		Integer idInt = IdValidator.validateAndParse(id);
		return bookRepository.findById(idInt);
	}

	@Override
    public Book create(Book book) {
//    	if (categoryRepository.existsByName(book.get)) {
//            throw new DuplicateResourceException("Name '" + category.getName() + "' already exists");
//        }
//    	category.setCode((category.getName()).toLowerCase().replaceAll(" ", "_"));
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book updateBook) {

        // Tìm category theo ID
    	Book existingBook = bookRepository.findById(updateBook.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục với id = " + updateBook.getId()));

        // Kiểm tra trùng name
//        if (updatedCategory.getName() != null &&
//        		categoryRepository.existsByNameAndIdNot(updatedCategory.getName(), updatedCategory.getId())) {
//            throw new DuplicateResourceException("Name '" + updatedCategory.getName() + "' đã tồn tại");
//        }
//        existingCategory.setCode((updatedCategory.getName()).toLowerCase().replaceAll(" ", "_"));

        // Cập nhật thông tin (nếu có truyền lên)
        if (updateBook.getTitle() != null) existingBook.setTitle(updateBook.getTitle());
        if (updateBook.getAuthor() != null) existingBook.setAuthor(updateBook.getAuthor());
        if (updateBook.getPublisher() != null) existingBook.setPublisher(updateBook.getPublisher());
        if (updateBook.getPublishedDate() != null) existingBook.setPublishedDate(updateBook.getPublishedDate());
        if (updateBook.getPageCount() != null) existingBook.setPageCount(updateBook.getPageCount());
        if (updateBook.getQuantity() != null) existingBook.setQuantity(updateBook.getQuantity());
        if (updateBook.getLanguage() != null) existingBook.setLanguage(updateBook.getLanguage());
        if (updateBook.getPrintType() != null) existingBook.setPrintType(updateBook.getPrintType());
        if (updateBook.getDescription() != null) existingBook.setDescription(updateBook.getDescription());
        if (updateBook.getThumbnailUrl() != null) existingBook.setThumbnailUrl(updateBook.getThumbnailUrl());
		if (updateBook.getBookCategories() != null) {
			existingBook.setBookCategories(new ArrayList<>());
			for (BookCategory bookCategory : updateBook.getBookCategories()) {
				BookCategory newBookCategory = new BookCategory();
				newBookCategory.setBook(existingBook);
				newBookCategory.setCategory(bookCategory.getCategory());
				existingBook.getBookCategories().add(newBookCategory);
			}
		}
		
        
        

        return bookRepository.save(updateBook);
    }

    
    @Override
    public void delete(String id) {
        Integer intId = IdValidator.validateAndParse(id);
        Book book = bookRepository.findById(intId)
            .orElseThrow(() -> new IdInvalidException("Không tìm thấy danh mục với id = " + intId));
        bookRepository.delete(book);
    }



}
