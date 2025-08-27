package com.vtit.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.request.comment.ReqCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCommentTreeDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;
import com.vtit.entity.Comment;
import com.vtit.entity.Post;
import com.vtit.exception.IdInvalidException;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.mapper.CommentMapper;
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
	private final CommentMapper commentMapper;

	public CommentServiceImpl(CommentRepository commentRepository
			, PostRepository postRepository
			, UserRepository userRepository
			, SecurityUtil securityUtil,
			CommentMapper commentMapper) {
		this.commentRepository = commentRepository;
		this.postRepository = postRepository;
		this.userRepository = userRepository;
		this.securityUtil = securityUtil;
		this.commentMapper = commentMapper;
	}
	
	@Override
	public ResultPaginationDTO findAll(Specification<Comment> spec, Pageable pageable) {
		Page<Comment> pageComments = commentRepository.findAll(spec, pageable);
		ResultPaginationDTO rs = new ResultPaginationDTO();
		ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
		List<ResCommentDTO> commentDTOs = pageComments.getContent().stream()
				.map(commentMapper::convertToResCommentDTO)
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
        		.orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        return commentMapper.convertToResCommentDTO(comment);
    }

    @Override
    public ResCreateCommentDTO create(ReqCreateCommentDTO request) {
        return commentMapper.convertToResCreateCommentDTO(commentRepository.save(commentMapper.convertToEntity(request)));
    }

    @Override
    public ResUpdateCommentDTO update(ReqUpdateCommentDTO comment) {
        Comment commentDB = commentRepository.findById(comment.getId())
        		.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy Comment với id = " + comment.getId()));
        return commentMapper.convertToResUpdateCommentDTO(commentRepository.save(commentMapper.convertToEntity(comment, commentDB)));
    }

    @Override
    public void delete(String id) {
        Integer commentId = IdValidator.validateAndParse(id);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        comment.setIsDeleted(true);
        commentRepository.save(comment);
    }
    
    @Override
    public List<ResCommentTreeDTO> getCommentTree(Integer postId) {
        List<Comment> roots = commentRepository.findByPostIdAndParentIsNullAndIsDeletedFalse(postId);
        return roots.stream()
            .map(this::convertToTreeDTO)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ResCommentTreeDTO> getCommentTreeByPostId(Integer postId) {
        List<Comment> allComments = commentRepository.findByPostId(postId);
        
        Map<Integer, ResCommentTreeDTO> dtoMap = new HashMap<>();
        List<ResCommentTreeDTO> rootComments = new ArrayList<>();

        for (Comment comment : allComments) {
            ResCommentTreeDTO dto = convertToTreeDTO(comment);
            dto.setReplies(new ArrayList<>());
            dtoMap.put(comment.getId(), dto);
        }

        for (Comment comment : allComments) {
            if (comment.getParent() != null) {
                ResCommentTreeDTO parentDTO = dtoMap.get(comment.getParent().getId());
                parentDTO.getReplies().add(dtoMap.get(comment.getId()));
            } else {
                rootComments.add(dtoMap.get(comment.getId()));
            }
        }

        return rootComments;
    }


    private ResCommentTreeDTO convertToTreeDTO(Comment comment) {
        ResCommentTreeDTO dto = new ResCommentTreeDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setCreatedBy(comment.getCreatedBy());
        dto.setCreatedDate(comment.getCreatedDate());
        dto.setUpdateBy(comment.getUpdatedBy());
        dto.setUpdateDate(comment.getUpdatedDate());
        return dto;
    }

	
	
	

}
