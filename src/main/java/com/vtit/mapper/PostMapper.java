package com.vtit.mapper;

import org.springframework.stereotype.Component;

import com.vtit.dto.response.User.ResUserSummartDTO;
import com.vtit.dto.response.post.ResCreatePostDTO;
import com.vtit.dto.response.post.ResPostDTO;
import com.vtit.dto.response.post.ResUpdatePostDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;
import com.vtit.entity.Post;
import com.vtit.service.CommentService;
import com.vtit.service.PostReactionService;

@Component
public class PostMapper {
	
	private final PostReactionService postReactionService;
	private final CommentService commentService;
	
	public PostMapper(PostReactionService postReactionService, CommentService commentService) {
		this.commentService = commentService;
		this.postReactionService = postReactionService;
	}
	
	public ResPostDTO convertToResPostDTO(Post post) {
        if (post == null) return null;

        ResPostDTO dto = new ResPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setCreatedBy(post.getCreatedBy());
        dto.setCreatedDate(post.getCreatedDate());
        dto.setUpdatedBy(post.getUpdatedBy());
        dto.setUpdatedDate(post.getUpdatedDate());

        if (post.getUser() != null) {
            ResUserSummartDTO userDTO = new ResUserSummartDTO();
            userDTO.setFullname(post.getUser().getFullname());
            dto.setUser(userDTO);
        }
        
        String sID = post.getId().toString();
        ReactionSummaryDTO reactionSummaryDTO = postReactionService.getReactionSummary(sID);
        dto.setLikeOrDislike(reactionSummaryDTO);
        dto.setComments(commentService.getCommentTreeByPostId(post.getId()));

        return dto;
    }
    
    public ResCreatePostDTO convertToResCreatePostDTO(Post post) {
        ResCreatePostDTO dto = new ResCreatePostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUser(post.getUser() != null ? post.getUser().getId() : null);
        dto.setCreatedBy(post.getCreatedBy());
        dto.setCreatedDate(post.getCreatedDate());
        return dto;
    }
    
    public ResUpdatePostDTO convertToResUpdatePostDTO(Post post) {
        ResUpdatePostDTO dto = new ResUpdatePostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUser(post.getUser() != null ? post.getUser().getId() : null);
        dto.setUpdatedBy(post.getUpdatedBy());
        dto.setUpdatedDate(post.getUpdatedDate());
        return dto;
    }
}
