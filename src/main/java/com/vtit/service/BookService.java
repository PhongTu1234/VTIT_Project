package com.vtit.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.entity.Book;

public interface BookService {
    void syncBooksFromGoogleApi();
    boolean existsByTitleAndAuthor(String title, String author);
    ResultPaginationDTO findAll(Specification<Book> spec, Pageable pageable);
	Optional<Book> findById(String id);
	Book create(Book book);
	Book update(Book book);
    void delete(String id);
}
