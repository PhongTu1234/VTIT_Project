package com.vtit.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Book;
import com.vtit.entity.Borrowing;

public interface BorrowingRepository extends JpaRepository<Borrowing, Integer> {
	List<Borrowing> findByBook(Book book);
}
