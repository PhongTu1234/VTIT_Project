package com.vtit.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.comment.ReqCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;
import com.vtit.entity.Comment;
import com.vtit.entity.Post;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.CommentRepository;
import com.vtit.reponsitory.PostRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.CommentService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

@Service
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final SecurityUtil securityUtil;

	public CommentServiceImpl(CommentRepository commentRepository
			, PostRepository postRepository
			, UserRepository userRepository
			, SecurityUtil securityUtil) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.securityUtil = securityUtil;
	}
	
	@Override
	public ResultPaginationDTO findAll(Specification<Comment> spec, Pageable pageable) {
		Page<Comment> pageComments = commentRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		List<ResCommentDTO> commentDTOs = pageComments.getContent().stream()
				.map(this::convertToResCommentDTO)
				.collect(Collectors.toList());
		
		mt.setPage(pageable.getPageNumber() + 1);
		mt.setPageSize(pageable.getPageSize());
		mt.setPages(pageComments.getTotalPages());
		mt.setTotals((int) pageComments.getTotalElements());
		
		rs.setMeta(mt);
		rs.setResult(commentDTOs);
		return rs;
	}
	
    @Override
    public ResCommentDTO findById(String id) {
    	Integer commentId = IdValidator.validateAndParse(id);
    	Comment comment = commentRepository.findById(commentId)
        		.orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + commentId));
        return convertToResCommentDTO(comment);
    }

    @Override
    public ResCreateCommentDTO create(ReqCreateCommentDTO request) {
        return convertToResCreateCommentDTO(commentRepository.save(convertToEntity(request)));
    }

    @Override
    public ResUpdateCommentDTO update(ReqUpdateCommentDTO comment) {
        Comment commentDB = commentRepository.findById(comment.getId())
        		.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Comment với id = " + comment.getId()));
        return convertToResUpdateCommentDTO(commentRepository.save(convertToEntity(comment, commentDB)));
    }

    @Override
    public void delete(String id) {
    	Integer postId = IdValidator.validateAndParse(id);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        post.setIsDeleted(true);
        postRepository.save(post);
    }

	public Comment convertToEntity(ReqCreateCommentDTO dto) {
		Comment comment = new Comment();
		comment.setContent(dto.getContent());
		Post postDB = postRepository.findById(dto.getPostId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Post với id = " + dto.getPostId()));
		comment.setPost(postDB);
		comment.setUser(userRepository.findByEmail(securityUtil.getCurrentUserLogin().get()));
		return comment;
	}

	public Comment convertToEntity(ReqUpdateCommentDTO dto, Comment comment) {
		comment.setContent(dto.getContent() != null ? dto.getContent() : null);
		return comment;
	}

	public ResCreateCommentDTO convertToResCreateCommentDTO(Comment comment) {
		ResCreateCommentDTO dto = new ResCreateCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		
		Post postDB = postRepository.findById(comment.getPost().getId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Post với id = " + comment.getPost().getId()));
		ResPostSummaryDTO postSummaryDTO = new ResPostSummaryDTO();
		postSummaryDTO.setId(postDB.getId());
		postSummaryDTO.setContent(postDB.getContent());
		postSummaryDTO.setAuthorName(postDB.getUser().getFullname());
		dto.setPost(postSummaryDTO);
		
//		Users userDB = userRepository.findByEmail(securityUtil.getCurrentUserLogin().get());
//		ResUserSummartDTO resUserSummartDTO = new ResUserSummartDTO();
//		resUserSummartDTO.setFullname(userDB.getFullname());
//		dto.setUser(resUserSummartDTO);
		
		dto.setCreatedBy(comment.getCreatedBy());
		dto.setCreatedDate(comment.getCreatedDate());
		return dto;
	}

	public ResUpdateCommentDTO convertToResUpdateCommentDTO(Comment comment) {
		ResUpdateCommentDTO dto = new ResUpdateCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		
		Post postDB = postRepository.findById(comment.getPost().getId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Post với id = " + comment.getPost().getId()));
		ResPostSummaryDTO postSummaryDTO = new ResPostSummaryDTO();
		postSummaryDTO.setId(postDB.getId());
		postSummaryDTO.setContent(postDB.getContent());
		postSummaryDTO.setAuthorName(postDB.getUser().getFullname());
		dto.setPost(postSummaryDTO);
		
		dto.setUpdatedBy(comment.getUpdatedBy());
		dto.setUpdatedDate(comment.getUpdatedDate());
		return dto;
	}

	public ResCommentDTO convertToResCommentDTO(Comment comment) {
		ResCommentDTO dto = new ResCommentDTO();
		dto.setId(comment.getId());
		dto.setContent(comment.getContent());
		
		Post postDB = postRepository.findById(comment.getPost().getId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Post với id = " + comment.getPost().getId()));
		ResPostSummaryDTO postSummaryDTO = new ResPostSummaryDTO();
		postSummaryDTO.setId(postDB.getId());
		postSummaryDTO.setContent(postDB.getContent());
		postSummaryDTO.setAuthorName(postDB.getUser().getFullname());
		dto.setPost(postSummaryDTO);
		
		dto.setCreatedBy(comment.getCreatedBy());
		dto.setCreatedDate(comment.getCreatedDate());
		dto.setUpdatedBy(comment.getUpdatedBy());
		dto.setUpdatedDate(comment.getUpdatedDate());
		return dto;
	}

}
