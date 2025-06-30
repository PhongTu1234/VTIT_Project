package com.vtit.service;

import java.util.List;

import com.vtit.entity.Book;

public interface BookService {
	Book save(Book book);
    Book findById(Integer id);
    List<Book> findAll();
    void deleteById(Integer id);
    
    void syncBooksFromGoogleApi();
    
    boolean existsByTitleAndAuthor(String title, String author);
}
