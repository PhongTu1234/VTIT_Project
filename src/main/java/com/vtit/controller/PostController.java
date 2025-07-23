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
import com.vtit.dto.response.post.ResPostReactionDTO;
import com.vtit.dto.response.post.ResPostSummaryDTO;
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
	
	public PostController(PostReactionService postReactionService, PostService postService) {
		this.postReactionService = postReactionService;
		this.postService = postService;
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
    public ResponseEntity<ResPostReactionDTO> reactToPost(@PathVariable String postId,
                                         @PathVariable String reaction) {
        postReactionService.reactToPost(postId, reaction);
        return ResponseEntity.ok(postReactionService.reactToPost(postId, reaction));
    }

    @GetMapping("/{postId}/reaction-summary")
    public ResponseEntity<?> getReactionSummary(@PathVariable String postId) {
        return ResponseEntity.ok(postReactionService.getReactionSummary(postId));
    }

}
