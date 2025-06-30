package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.reponsitory.PostRepository;
import com.vtit.service.PostService;

@Service
public class PostServiceImpl implements PostService{
	private final PostRepository postRepository;
	
	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
}
