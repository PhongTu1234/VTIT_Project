package com.vtit.service;

import com.vtit.dto.response.post.ResPostReactionDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;

public interface PostReactionService {
	ResPostReactionDTO reactToPost(String postId, String reactionType);
	ReactionSummaryDTO getReactionSummary(String postId);
}
