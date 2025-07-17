package com.vtit.controller;

import org.apache.http.HttpStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.entity.Book;
import com.vtit.service.BookService;
import com.vtit.utils.annotation.ApiMessage;

import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<Book> getById(@PathVariable String id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }
	
	@PostMapping
	@ApiMessage("Create a new Book")
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(bookService.create(book));
    }
	
	@PutMapping()
	@ApiMessage("Update a Book")
    public ResponseEntity<Book> update(@Valid @RequestBody Book book) {
        return ResponseEntity.ok(bookService.update(book));
    }
    
    @DeleteMapping("/{id}")
    @ApiMessage("Delete a Book by ID")
    public void delete(@PathVariable String id) {
    	bookService.delete(id);
    }
}
