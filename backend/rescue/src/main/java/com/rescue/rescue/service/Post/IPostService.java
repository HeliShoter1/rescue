package com.rescue.rescue.service.Post;

import java.util.List;

import com.rescue.rescue.dto.PostDto;
import com.rescue.rescue.request.CreatePost;
import com.rescue.rescue.request.UpdatePost;

public interface IPostService {
    List<PostDto> getAllPost(Long cursor, Integer limit);
    PostDto createPost(CreatePost post);    
    void updatePost( UpdatePost post);
}
