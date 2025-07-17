package com.vtit.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.entity.Post;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.PostRepository;
import com.vtit.service.PostService;
import com.vtit.utils.IdValidator;

@Service
public class PostServiceImpl implements PostService{
	private final PostRepository postRepository;
	
	public PostServiceImpl(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	
	@Override
	public ResultPaginationDTO findAll(Specification<Post> spec, Pageable pageable) {
		Page<Post> pagePosts = postRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pagePosts.getTotalPages());
		mt.setTotals((int) pagePosts.getTotalElements());
		
		rs.setMeta(mt);
		rs.setResult(pagePosts.getContent());
		return rs;
	}
	
    @Override
    public Post findById(String id) {
    	Integer postId = IdValidator.validateAndParse(id);
        return postRepository.findById(postId)
        		.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
    }

    @Override
    public Post create(Post request) {
        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        return postRepository.save(post);
    }

    @Override
    public Post update(Post post) {
        Post postDB = postRepository.findById(post.getId())
        		.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Post với id = " + post.getId()));

        post.setTitle(postDB.getTitle());
        post.setContent(postDB.getContent());
        post.setIsActive(postDB.getIsActive());

        return postRepository.save(post);
    }

    @Override
    public void delete(String id) {
    	Integer postId = IdValidator.validateAndParse(id);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setIsDeleted(true);
        postRepository.save(post);
    }

}
