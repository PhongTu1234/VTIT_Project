package com.vtit.service.impl;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.vtit.dto.request.comment.RepCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.entity.Comment;
import com.vtit.reponsitory.CommentRepository;
import com.vtit.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	

	public Comment convertToEntity(RepCreateCommentDTO dto) {
		Comment comment = new Comment();
		comment.setContent(dto.getContent());
		comment.setPostId(dto.getPostId());
		comment.setUserId(dto.getUserId());
		comment.setCreatedDate(Instant.now());
		return comment;
	}

	public Comment convertToEntity(ReqUpdateCommentDTO dto) {
		Comment comment = new Comment();
		comment.setId(dto.getId());
		comment.setContent(dto.getContent());
		comment.setPostId(dto.getPostId());
		comment.setUserId(dto.getUserId());
		comment.setUpdatedDate(Instant.now());
		return comment;
	}

	public ResCreateCommentDTO convertToResCreateCommentDTO(Comment comment) {
		ResCreateCommentDTO dto = new ResCreateCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		dto.setPostId(comment.getPostId());
		dto.setUserId(comment.getUserId());
		dto.setCreatedBy(comment.getCreatedBy());
		dto.setCreatedDate(comment.getCreatedDate());
		return dto;
	}

	public ResUpdateCommentDTO convertToResUpdateCommentDTO(Comment comment) {
		ResUpdateCommentDTO dto = new ResUpdateCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		dto.setPostId(comment.getPostId());
		dto.setUserId(comment.getUserId());
		dto.setUpdatedBy(comment.getUpdatedBy());
		dto.setUpdatedDate(comment.getUpdatedDate());
		return dto;
	}

	public ResCommentDTO convertToResCommentDTO(Comment comment) {
		ResCommentDTO dto = new ResCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		dto.setPostId(comment.getPostId());
		dto.setUserId(comment.getUserId());
		dto.setCreatedBy(comment.getCreatedBy());
		dto.setCreatedDate(comment.getCreatedDate());
		dto.setUpdatedBy(comment.getUpdatedBy());
		dto.setUpdatedDate(comment.getUpdatedDate());
		return dto;
	}

}
