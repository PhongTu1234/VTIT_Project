package com.vtit.reponsitory;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.vtit.dto.response.category.ResCategoryBookStatisticDTO;
import com.vtit.dto.response.post.ResTopPostDTO;
import com.vtit.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book>{
	boolean existsByTitleAndAuthor(String title, String author);
	boolean existsByTitleAndAuthorAndPublisherAndPublishedDate(
	        String title, String author, String publisher, Instant publishedDate
	    );
	@Query("SELECT new com.vtit.dto.response.category.ResCategoryBookStatisticDTO(c.name, COUNT(b)) " +
	           "FROM Book b JOIN b.category c " +
	           "WHERE b.isActive = true AND b.isDeleted = false " +
	           "GROUP BY c.name")
	    List<ResCategoryBookStatisticDTO> getCategoryBookStatistics();
	
	




//	Optional<Book> findByName(String name);
//	boolean existsByName(String name);
//	boolean existsByCode(String code);
//	boolean existsByNameAndIdNot(String name, Integer id);
//	boolean existsByCodeAndIdNot(String code, Integer id);
}
