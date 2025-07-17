package com.vtit.service.impl;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

@Service
public class PostReactionServiceImpl implements PostReactionService {

	private final PostReactionRepository postReactionRepository;
	private final PostRepository postRepository;
	private final UserRepository userRepository;

	public PostReactionServiceImpl(PostReactionRepository postReactionRepository, UserRepository userRepository,
			PostRepository postRepository) {
		this.postReactionRepository = postReactionRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;

	}

	public void reactToPost(String postId, Integer userId, String reactionType) {
		if (!reactionType.equalsIgnoreCase("LIKE") && !reactionType.equalsIgnoreCase("DISLIKE")) {
			throw new IllegalArgumentException("Reaction type must be LIKE or DISLIKE");
		}

		Integer postIdInt = IdValidator.validateAndParse(postId);

		Post post = postRepository.findById(postIdInt)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found"));
		Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
	}

	public ReactionSummaryDTO getReactionSummary(String postId) {
		Integer postIdInt = IdValidator.validateAndParse(postId);
		Post post = postRepository.findById(postIdInt)
				.orElseThrow(() -> new ResourceNotFoundException("Post not found"));

		long likes = postReactionRepository.countByPostAndReactionType(post, "LIKE");
		long dislikes = postReactionRepository.countByPostAndReactionType(post, "DISLIKE");

		return new ReactionSummaryDTO(likes, dislikes);
	}
}
