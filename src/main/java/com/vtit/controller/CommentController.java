package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.borrowing.ReqCreateBorrowingDTO;
import com.vtit.dto.request.borrowing.ReqUpdateBorrowingDTO;
import com.vtit.dto.request.comment.ReqCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.borrowing.ResBorrowingDTO;
import com.vtit.dto.response.borrowing.ResCreateBorrowingDTO;
import com.vtit.dto.response.borrowing.ResUpdateBorrowingDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.entity.Borrowing;
import com.vtit.entity.Comment;
import com.vtit.service.BorrowingService;
import com.vtit.service.CommentService;

@RestController
@RequestMapping("/api/v1/library/comments")
public class CommentController {

    private final CommentService commentService;
    
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

    @GetMapping
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Comment> spec, Pageable pageable) {
        return ResponseEntity.ok(commentService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResCommentDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResCreateCommentDTO> create(@RequestBody ReqCreateCommentDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @PutMapping
    public ResponseEntity<ResUpdateCommentDTO> update(@RequestBody ReqUpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
    	commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
