package com.vtit.reponsitory;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.vtit.dto.response.post.ResTopPostDTO;
import com.vtit.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
	@Query("SELECT new com.vtit.dto.response.post.ResTopPostDTO(pr.post.id, pr.post.title, COUNT(pr)) " +
	           "FROM PostReaction pr " +
	           "GROUP BY pr.post.id, pr.post.title " +
	           "ORDER BY COUNT(pr) DESC")
	    List<ResTopPostDTO> findTopLikedPosts(Pageable pageable);
}
