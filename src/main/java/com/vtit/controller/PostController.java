package com.vtit.controller;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.vtit.dto.request.post.ReqCreatePostDTO;
import com.vtit.dto.request.post.ReqUpdatePostDTO;
import com.vtit.dto.response.post.ResCreatePostDTO;
import com.vtit.dto.response.post.ResPostDTO;
import com.vtit.dto.response.post.ResPostReactionDTO;
import com.vtit.dto.response.post.ResTopPostDTO;
import com.vtit.dto.response.post.ResUpdatePostDTO;
import com.vtit.dto.response.postReaction.ReactionSummaryDTO;
import com.vtit.entity.Post;
import com.vtit.service.PostReactionService;
import com.vtit.service.PostService;
import com.vtit.utils.annotation.ApiMessage;

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
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("Get list of posts")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Post> spec, Pageable pageable) {
        return ResponseEntity.ok(postService.findAll(spec, pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get post by ID")
    public ResponseEntity<ResPostDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Create a new post")
    public ResponseEntity<ResCreatePostDTO> create(@RequestBody ReqCreatePostDTO post) {
        return ResponseEntity.ok(postService.create(post));
    }

    @PutMapping
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Update a post")
    public ResponseEntity<ResUpdatePostDTO> update(@RequestBody ReqUpdatePostDTO post) {
        return ResponseEntity.ok(postService.update(post));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Delete a post by ID")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
	
	@PostMapping("/{postId}/{reaction}")
	@PreAuthorize("@customPermissionEvaluator.check(authentication)")
	@ApiMessage("React to a post")
    public ResponseEntity<ResPostReactionDTO> reactToPost(@PathVariable String postId,
                                         @PathVariable String reaction) {
        return ResponseEntity.ok(postReactionService.reactToPost(postId, reaction));
    }

    @GetMapping("/{postId}/reaction-summary")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get reaction summary for a post")
    public ResponseEntity<ReactionSummaryDTO> getReactionSummary(@PathVariable String postId) {
        return ResponseEntity.ok(postReactionService.getReactionSummary(postId));
    }
    
    @GetMapping("/top-liked")
    @PreAuthorize("@customPermissionEvaluator.check(authentication)")
    @ApiMessage("Get top 5 liked posts")
    public ResponseEntity<List<ResTopPostDTO>> getTopLikedPosts() {
        List<ResTopPostDTO> topPosts = postService.getTop5LikedPosts();
        return ResponseEntity.ok(topPosts);
    }

}
