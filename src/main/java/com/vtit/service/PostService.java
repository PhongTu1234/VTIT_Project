package com.vtit.service;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.post.PostDTO;
import com.vtit.entity.Post;

public interface PostService {
	ResultPaginationDTO findAll(Specification<Post> spec, Pageable pageable);
    Post findById(String id);
    Post create(Post post);
    Post update(Post post);
    void delete(String id);
}
