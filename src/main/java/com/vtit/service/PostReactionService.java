package com.vtit.service;

import com.vtit.dto.response.postReaction.ReactionSummaryDTO;

public interface PostReactionService {
	void reactToPost(String postId, Integer userId, String reactionType);
	ReactionSummaryDTO getReactionSummary(String postId);
}
