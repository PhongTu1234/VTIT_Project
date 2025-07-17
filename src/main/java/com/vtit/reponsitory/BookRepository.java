package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vtit.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book>{
	boolean existsByTitleAndAuthor(String title, String author);
//	Optional<Book> findByName(String name);
//	boolean existsByName(String name);
//	boolean existsByCode(String code);
//	boolean existsByNameAndIdNot(String name, Integer id);
//	boolean existsByCodeAndIdNot(String code, Integer id);
}
