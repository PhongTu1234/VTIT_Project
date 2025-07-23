package com.vtit.service.impl;

import org.springframework.stereotype.Service;

import com.vtit.dto.request.comment.RepCreateCommentDTO;
import com.vtit.dto.request.comment.ReqUpdateCommentDTO;
import com.vtit.dto.response.comment.ResCommentDTO;
import com.vtit.dto.response.comment.ResCreateCommentDTO;
import com.vtit.dto.response.comment.ResUpdateCommentDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;
import com.vtit.entity.Comment;
import com.vtit.entity.Post;
import com.vtit.exception.IdInvalidException;
import com.vtit.reponsitory.CommentRepository;
import com.vtit.reponsitory.PostRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.CommentService;
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
	
	

	public Comment convertToEntity(RepCreateCommentDTO dto) {
		Comment comment = new Comment();
		comment.setContent(dto.getContent());
		comment.setPost(postRepository.findById(dto.getPostId()).isPresent() ? postRepository.findById(dto.getPostId()).get() : null);
		comment.setUser(userRepository.findByEmail(securityUtil.getCurrentUserLogin().get()));
		return comment;
	}

	public Comment convertToEntity(ReqUpdateCommentDTO dto) {
		Comment comment = commentRepository.findById(dto.getId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Comment với id = " + dto.getId()));
		comment.setContent(dto.getContent());
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
