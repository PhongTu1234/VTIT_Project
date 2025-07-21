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
import com.vtit.dto.book.ReqCreateBookDTO;
import com.vtit.dto.book.ReqUpdateBookDTO;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResCreateBookDTO;
import com.vtit.dto.response.book.ResUpdateBookDTO;
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
    public ResponseEntity<ResBookDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookService.findById(id));
    }
	
	@PostMapping
	@ApiMessage("Create a new Book")
    public ResponseEntity<ResCreateBookDTO> create(@Valid @RequestBody ReqCreateBookDTO book) {
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(bookService.create(book));
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
}
