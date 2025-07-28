package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.comment.ReqCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.entity.Comment;

public interface CommentService {
	ResultPaginationDTO findAll(Specification<Comment> spec, Pageable pageable);
	ResCommentDTO findById(String id);
	ResUpdateCommentDTO update(ReqUpdateCommentDTO dto);
    void delete(String id);
	ResCreateCommentDTO create(ReqCreateCommentDTO dto);
}
