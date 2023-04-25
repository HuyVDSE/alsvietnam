package com.alsvietnam.repository;

import com.alsvietnam.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, String>, CommentRepositoryCustom {
    Set<Comment> findByUsers_Id(String userId);
}
