package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("""
        SELECT p 
        FROM Post p 
        WHERE p.id >= :cursor 
        ORDER BY p.id ASC
        limit :limit
    """)
    List<Post> getAllPost(Long cursor, Integer limit);

}
