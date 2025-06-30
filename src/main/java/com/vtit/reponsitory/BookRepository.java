package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
	boolean existsByTitleAndAuthor(String title, String author);
}
