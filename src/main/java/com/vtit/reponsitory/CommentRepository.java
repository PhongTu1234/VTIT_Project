package com.vtit.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.vtit.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {
	List<Comment> findByPostIdAndParentIsNullAndIsDeletedFalse(Integer postId);
	List<Comment> findByPostId(Integer postId);

}
