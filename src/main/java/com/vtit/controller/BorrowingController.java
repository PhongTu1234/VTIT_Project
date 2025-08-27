package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.vtit.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/library/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    
	public BorrowingController(BorrowingService borrowingService) {
		this.borrowingService = borrowingService;
	}

    @GetMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Fetch all Borrowings")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Borrowing> spec, Pageable pageable) {
        return ResponseEntity.ok(borrowingService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Fetch a Borrowing by ID")
    public ResponseEntity<ResBorrowingDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(borrowingService.findById(id));
    }

    @PostMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Create a new Borrowing")
    public ResponseEntity<ResCreateBorrowingDTO> create(@RequestBody ReqCreateBorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.create(dto));
    }

    @PutMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Update a Borrowing")
    public ResponseEntity<ResUpdateBorrowingDTO> update(@RequestBody ReqUpdateBorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.update(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Delete a Borrowing by ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        borrowingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
