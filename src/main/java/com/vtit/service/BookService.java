package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.request.book.ReqCreateBookDTO;
import com.vtit.dto.request.book.ReqUpdateBookDTO;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.book.ResCreateBookDTO;
import com.vtit.dto.response.book.ResUpdateBookDTO;
import com.vtit.entity.Book;

public interface BookService {
    void syncBooksFromGoogleApi();
    boolean existsByTitleAndAuthor(String title, String author);
    ResultPaginationDTO findAll(Specification<Book> spec, Pageable pageable);
    ResBookDTO findById(String id);
	ResCreateBookDTO create(ReqCreateBookDTO dto);
	ResUpdateBookDTO update(ReqUpdateBookDTO dto);
    void delete(String id);
}
