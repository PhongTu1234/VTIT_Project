package com.vtit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.post.ReqCreatePostDTO;
import com.vtit.dto.request.post.ReqUpdatePostDTO;
import com.vtit.dto.response.post.ResCreatePostDTO;
import com.vtit.dto.response.post.ResPostDTO;
import com.vtit.dto.response.post.ResUpdatePostDTO;
import com.vtit.entity.Post;

public interface PostService {
	ResultPaginationDTO findAll(Specification<Post> spec, Pageable pageable);
    ResPostDTO findById(String id);
    ResCreatePostDTO create(ReqCreatePostDTO post);
    ResUpdatePostDTO update(ReqUpdatePostDTO dto);
    void delete(String id);
}
