package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

}
