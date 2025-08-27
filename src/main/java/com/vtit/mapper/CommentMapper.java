package com.vtit.mapper;

import org.springframework.stereotype.Component;

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
import com.vtit.utils.SecurityUtil;

@Component
public class CommentMapper {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;
	private final SecurityUtil securityUtil;

	public CommentMapper(PostRepository postRepository, CommentRepository commentRepository,
			UserRepository userRepository, SecurityUtil securityUtil) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.userRepository = userRepository;
		this.securityUtil = securityUtil;
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

	public Comment convertToEntity(ReqCreateCommentDTO dto) {
		Comment comment = new Comment();
		if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("Nội dung bình luận không được để trống");
		}
		comment.setContent(dto.getContent());

		if (dto.getParentId() != null) {
			Comment parent = commentRepository.findById(dto.getParentId())
					.orElseThrow(() -> new ResourceNotFoundException("Parent comment not found"));
			comment.setParent(parent);
		}

		Post postDB = postRepository.findById(dto.getPostId())
				.orElseThrow(() -> new IdInvalidException("Không tìm thấy Post với id = " + dto.getPostId()));
		comment.setPost(postDB);
		comment.setUser(userRepository.findByEmail(securityUtil.getCurrentUserLogin().get()));
		return comment;
	}

	public Comment convertToEntity(ReqUpdateCommentDTO dto, Comment comment) {
		if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
			throw new IllegalArgumentException("Nội dung bình luận không được để trống");
		}

		comment.setContent(dto.getContent());
		return comment;
	}
}
