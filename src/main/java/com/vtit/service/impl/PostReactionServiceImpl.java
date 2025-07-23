package com.vtit.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vtit.dto.response.post.ResPostReactionDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;
import com.vtit.entity.Post;
import com.vtit.entity.PostReaction;
import com.vtit.entity.Users;
import com.vtit.exception.ResourceNotFoundException;
import com.vtit.reponsitory.PostReactionRepository;
import com.vtit.reponsitory.PostRepository;
import com.vtit.reponsitory.UserRepository;
import com.vtit.service.PostReactionService;
import com.vtit.utils.IdValidator;
import com.vtit.utils.SecurityUtil;

@Service
public class PostReactionServiceImpl implements PostReactionService {

	private final PostReactionRepository postReactionRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final SecurityUtil securityUtil;

	public PostReactionServiceImpl(PostReactionRepository postReactionRepository, UserRepository userRepository,
			PostRepository postRepository, SecurityUtil securityUtil) {
		this.postReactionRepository = postReactionRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
		this.securityUtil = securityUtil;
	}

	public ResPostReactionDTO reactToPost(String postId, String reactionType) {
		if (!reactionType.equalsIgnoreCase("LIKE") && !reactionType.equalsIgnoreCase("DISLIKE")) {
			throw new IllegalArgumentException("Loại phản ứng phải là THÍCH hoặc KHÔNG THÍCH");
		}

		Integer postIdInt = IdValidator.validateAndParse(postId);

		Post post = postRepository.findById(postIdInt)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found"));
		Users user = userRepository.findByEmail(securityUtil.getCurrentUserLogin().get());

		Optional<PostReaction> existing = postReactionRepository.findByPostAndUser(post, user);
		if (existing.isPresent()) {
			PostReaction reaction = existing.get();
			if (reaction.getReactionType().equalsIgnoreCase(reactionType)) {
				postReactionRepository.delete(reaction); // Toggle off
			} else {
				reaction.setReactionType(reactionType.toUpperCase());
				postReactionRepository.save(reaction);
			}
		} else {
			PostReaction newReaction = new PostReaction();
			newReaction.setPost(post);
			newReaction.setUser(user);
			newReaction.setReactionType(reactionType.toUpperCase());
			newReaction.setCreatedAt(Instant.now());
			postReactionRepository.save(newReaction);
		}
		ResPostReactionDTO postSummary = new ResPostReactionDTO();
		postSummary.setId(post.getId());
		postSummary.setContent(post.getContent());
		postSummary.setAuthorName(post.getUser().getFullname());
		postSummary.setReactionType(reactionType.toUpperCase());
		return postSummary;
	}

	public ReactionSummaryDTO getReactionSummary(String postId) {
		Integer postIdInt = IdValidator.validateAndParse(postId);
		Post post = postRepository.findById(postIdInt)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found"));

		long likes = postReactionRepository.countByPostAndReactionType(post, "LIKE");
		long dislikes = postReactionRepository.countByPostAndReactionType(post, "DISLIKE");
		ReactionSummaryDTO reactionSummary = new ReactionSummaryDTO();
		reactionSummary.setPostId(postIdInt);
		reactionSummary.setPostTitle(post.getTitle() != null ? post.getTitle() : "No Title");
		reactionSummary.setAuthorName(post.getUser().getFullname() != null ? post.getUser().getFullname() : "Unknown Author");
		reactionSummary.setLikeCount(likes);
		reactionSummary.setDislikeCount(dislikes);
		
		return reactionSummary;
	}
}
