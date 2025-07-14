package com.vtit.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.vtit.entity.Book;
import com.vtit.entity.BookCategory;
import com.vtit.entity.Category;
import com.vtit.reponsitory.BookRepository;
import com.vtit.reponsitory.CategoryRepository;
import com.vtit.service.BookService;

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
	public Book save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Book findById(Integer id) {
		return bookRepository.findById(id).orElse(null);
	}

	@Override
	public List<Book> findAll() {
		return bookRepository.findAll();
	}

	@Override
	public void deleteById(Integer id) {
		bookRepository.deleteById(id);
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



}
