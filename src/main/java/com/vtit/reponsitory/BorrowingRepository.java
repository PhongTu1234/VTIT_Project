package com.vtit.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vtit.entity.Book;
import com.vtit.entity.Borrowing;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer>, JpaSpecificationExecutor<Borrowing> {
	List<Borrowing> findByBook(Book book);
}
