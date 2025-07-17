package com.vtit.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vtit.dto.common.ResultPaginationDTO;
import com.vtit.dto.response.post.PostDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;
import com.vtit.entity.Post;
import com.vtit.entity.Users;
import com.vtit.service.PostReactionService;
import com.vtit.service.PostService;
import com.vtit.service.UserService;
import com.vtit.utils.SecurityUtil;

@RestController
@RequestMapping("/api/v1/library/posts")
public class PostController {
	
	private final PostReactionService postReactionService;
	private final PostService postService;
	private final UserService userService;
	
	public PostController(PostReactionService postReactionService, PostService postService,
			UserService userService) {
		this.postReactionService = postReactionService;
		this.postService = postService;
		this.userService = userService;
	}
	
	@GetMapping
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Post> spec, Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable String id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        return ResponseEntity.ok(postService.create(post));
    }

    @PutMapping
    public ResponseEntity<Post> update(@RequestBody Post post) {
        return ResponseEntity.ok(postService.update(post));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@PostMapping("/{postId}/{reaction}")
    public ResponseEntity<?> reactToPost(@PathVariable String postId,
                                         @PathVariable String reaction) {
		String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
		Users currentUserDB = this.userService.handleGetUserByUsernames(email);
        try {
            postReactionService.reactToPost(postId, currentUserDB.getId(), reaction);
            return ResponseEntity.ok("Reaction recorded");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{postId}/reaction-summary")
    public ResponseEntity<?> getReactionSummary(@PathVariable String postId) {
        ReactionSummaryDTO summary = postReactionService.getReactionSummary(postId);
        return ResponseEntity.ok(summary);
    }

}
