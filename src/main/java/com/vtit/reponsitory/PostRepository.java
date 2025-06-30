package com.vtit.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {

}
