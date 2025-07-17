package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.borrowing.BorrowingDTO;
import com.vtit.entity.Borrowing;

public interface BorrowingService {
	ResultPaginationDTO findAll(Specification<Borrowing> spec, Pageable pageable);
    BorrowingDTO findById(Integer id);
    BorrowingDTO create(BorrowingDTO dto);
    BorrowingDTO update(Integer id, BorrowingDTO dto);
    void delete(String id);
}
