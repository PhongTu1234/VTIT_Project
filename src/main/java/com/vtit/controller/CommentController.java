package com.vtit.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.vtit.dto.response.comment.ResCommentTreeDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.entity.Borrowing;
import com.vtit.entity.Comment;
import com.vtit.service.BorrowingService;
import com.vtit.service.CommentService;
import com.vtit.utils.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/library/comments")
public class CommentController {

    private final CommentService commentService;
    
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

    @GetMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get list of comments")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Comment> spec, Pageable pageable) {
        return ResponseEntity.ok(commentService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get comment by id")
    public ResponseEntity<ResCommentDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(commentService.findById(id));
    }

    @PostMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Create a new comment")
    public ResponseEntity<ResCreateCommentDTO> create(@RequestBody ReqCreateCommentDTO dto) {
        return ResponseEntity.ok(commentService.create(dto));
    }

    @PutMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Update an existing comment")
    public ResponseEntity<ResUpdateCommentDTO> update(@RequestBody ReqUpdateCommentDTO dto) {
        return ResponseEntity.ok(commentService.update(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Delete a comment by id")
    public ResponseEntity<Void> delete(@PathVariable String id) {
    	commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/tree/{postId}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get comment tree for a specific post")
    public ResponseEntity<List<ResCommentTreeDTO>> getCommentTree(@RequestParam Integer postId) {
        return ResponseEntity.ok(commentService.getCommentTree(postId));
    }


}
