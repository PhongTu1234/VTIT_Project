package com.vtit.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.post.ReqCreatePostDTO;
import com.vtit.dto.request.post.ReqUpdatePostDTO;
import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.book.ResBookDTO;
import com.vtit.dto.response.post.ResCreatePostDTO;
import com.vtit.dto.response.post.ResPostDTO;
import com.vtit.dto.response.post.ResTopPostDTO;
import com.vtit.dto.response.post.ResUpdatePostDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;
import com.vtit.entity.Post;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.mapper.PostMapper;
import com.vtit.reponsitory.PostRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.CommentService;
import com.vtit.service.PostReactionService;
import com.vtit.service.PostService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

@Service
public class PostServiceImpl implements PostService{
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final SecurityUtil securityUtil;
	private final PostReactionService postReactionService;
	private final CommentService commentService;
	private final PostMapper postMapper;
	
	public PostServiceImpl(PostRepository postRepository,
			UserRepository userRepository,
			SecurityUtil securityUtil,
			PostReactionService postReactionService,
			CommentService commentService,
			PostMapper postMapper) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.securityUtil = securityUtil;
		this.postReactionService = postReactionService;
		this.commentService = commentService;
		this.postMapper = postMapper;
	}
	
	@Override
	public ResultPaginationDTO findAll(Specification<Post> spec, Pageable pageable) {
		Page<Post> pagePosts = postRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		List<ResPostDTO> postDTOs = pagePosts.getContent().stream()
				.map(postMapper::convertToResPostDTO)
				.collect(Collectors.toList());
		
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pagePosts.getTotalPages());
		mt.setTotals((int) pagePosts.getTotalElements());
		
		rs.setMeta(mt);
		rs.setResult(postDTOs);
		return rs;
	}
	
    @Override
    public ResPostDTO findById(String id) {
    	Integer postId = IdValidator.validateAndParse(id);
    	Post post = postRepository.findById(postId)
        		.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));
        return postMapper.convertToResPostDTO(post);
    }

    @Override
    public ResCreatePostDTO create(ReqCreatePostDTO request) {
        return postMapper.convertToResCreatePostDTO(postRepository.save(convertToEntity(request)));
    }

    @Override
    public ResUpdatePostDTO update(ReqUpdatePostDTO post) {
        Post postDB = postRepository.findById(post.getId())
        		.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Post với id = " + post.getId()));
        return postMapper.convertToResUpdatePostDTO(postRepository.save(convertToEntity(post, postDB)));
    }

    @Override
    public void delete(String id) {
    	Integer postId = IdValidator.validateAndParse(id);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setIsDeleted(true);
        postRepository.save(post);
    }
    
    public Post convertToEntity(ReqCreatePostDTO dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setUser(userRepository.findByUsername(securityUtil.getCurrentUserLogin().get()));
        return post;
    }
    
    public Post convertToEntity(ReqUpdatePostDTO dto, Post existingPost) {
        if (dto.getTitle() != null) {
            existingPost.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            existingPost.setContent(dto.getContent());
        }
        return existingPost;
    }
    
    @Override
    public List<ResTopPostDTO> getTop5LikedPosts() {
        return postRepository.findTopLikedPosts(PageRequest.of(0, 5));
    }

}
