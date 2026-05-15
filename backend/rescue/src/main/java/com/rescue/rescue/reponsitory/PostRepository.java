package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rescue.rescue.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findAll(Long Cursor, Integer limit);
}
