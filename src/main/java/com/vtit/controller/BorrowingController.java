package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.borrowing.ReqCreateBorrowingDTO;
import com.vtit.dto.request.borrowing.ReqUpdateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.entity.Borrowing;
import com.vtit.service.BorrowingService;

@RestController
@RequestMapping("/api/v1/library/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    
	public BorrowingController(BorrowingService borrowingService) {
		this.borrowingService = borrowingService;
	}

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Borrowing> spec, Pageable pageable) {
        return ResponseEntity.ok(borrowingService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResBorrowingDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(borrowingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResCreateBorrowingDTO> create(@RequestBody ReqCreateBorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.create(dto));
    }

    @PutMapping
    public ResponseEntity<ResUpdateBorrowingDTO> update(@RequestBody ReqUpdateBorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        borrowingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
