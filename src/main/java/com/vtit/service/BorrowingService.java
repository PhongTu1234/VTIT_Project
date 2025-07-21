package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.borrowing.ReqCreateBorrowingDTO;
import com.vtit.dto.request.borrowing.ReqUpdateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.entity.Borrowing;

public interface BorrowingService {
	ResultPaginationDTO findAll(Specification<Borrowing> spec, Pageable pageable);
    ResBorrowingDTO findById(Integer id);
    ResCreateBorrowingDTO create(ReqCreateBorrowingDTO dto);
    ResUpdateBorrowingDTO update(ReqUpdateBorrowingDTO dto);
    void delete(String id);
}
