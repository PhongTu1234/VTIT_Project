package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.CommentRepository;
import com.vtit.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{
	private final CommentRepository commentRepository;
	
	public CommentServiceImpl(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
}
