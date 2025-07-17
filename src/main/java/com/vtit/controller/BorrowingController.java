package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.borrowing.BorrowingDTO;
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
    public ResponseEntity<ResultPaginationDTO> getAll(Specification<Borrowing> spec, Pageable pageable) {
        return ResponseEntity.ok(borrowingService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingDTO> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(borrowingService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BorrowingDTO> create(@RequestBody BorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowingDTO> update(@PathVariable Integer id, @RequestBody BorrowingDTO dto) {
        return ResponseEntity.ok(borrowingService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        borrowingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
