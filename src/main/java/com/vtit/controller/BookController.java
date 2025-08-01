package com.vtit.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.request.book.ReqCreateBookDTO;
import com.vtit.dto.request.book.ReqUpdateBookDTO;
import com.vtit.dto.common.RestResponseDTO;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResCreateBookDTO;
import com.vtit.dto.response.book.ResUpdateBookDTO;
import com.vtit.dto.response.category.ResCategoryBookStatisticDTO;
import com.vtit.entity.Book;
import com.vtit.service.BookService;
import com.vtit.utils.annotation.ApiMessage;
import com.vtit.utils.excel.BookExcelExporter;
import com.vtit.utils.excel.BookExcelImporter;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/library/books")
public class BookController {
	private final BookService bookService;
	
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@GetMapping
	@ApiMessage("Fresh all Books")
	public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Book> spec, Pageable pageable){
		return ResponseEntity.ok(bookService.findAll(spec, pageable));
	}
	
	@GetMapping("/{id}")
	@ApiMessage("Fresh a Book by ID")
    public ResponseEntity<ResBookDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findById(id));
    }
	
	@PostMapping
	@ApiMessage("Create a new Book")
    public ResponseEntity<ResCreateBookDTO> create(@Valid @RequestBody ReqCreateBookDTO book) {
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(bookService.create(book));
    }
	
	@PutMapping()
	@ApiMessage("Update a Book")
    public ResponseEntity<ResUpdateBookDTO> update(@Valid @RequestBody ReqUpdateBookDTO book) {
        return ResponseEntity.ok(bookService.update(book));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessage("Delete a Book by ID")
    public void delete(@PathVariable String id) {
    	bookService.delete(id);
    }
    
    @GetMapping("/statistics-by-category")
    @ApiMessage("Get book statistics by category")
    public ResponseEntity<List<ResCategoryBookStatisticDTO>> getBookStatistics() {
        List<ResCategoryBookStatisticDTO> data = bookService.getCategoryBookStatistics();
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/export")
    @ApiMessage("Export all books to Excel file")
    public ResponseEntity<InputStreamResource> exportBooksToExcel() {
        ByteArrayInputStream in = bookService.exportBooksToExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=books.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }
    
    @PostMapping("/import")
    @ApiMessage("Import books from Excel file")
    public ResponseEntity<RestResponseDTO<String>> importBooks(@RequestParam("file") MultipartFile file) {
        bookService.importBooksFromExcel(file);

        RestResponseDTO<String> response = new RestResponseDTO<>();
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Import sách thành công");
        response.setData(null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
