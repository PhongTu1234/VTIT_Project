package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {

}
