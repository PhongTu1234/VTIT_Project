package com.vtit.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vtit.entity.Post;
import com.vtit.entity.PostReaction;
import com.vtit.entity.Users;

public interface PostReactionRepository extends JpaRepository<PostReaction, Integer> {
    Optional<PostReaction> findByPostAndUser(Post post, Users user);
    long countByPostAndReactionType(Post post, String type);
}
