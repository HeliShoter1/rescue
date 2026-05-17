package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.dto.PostDto;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.CreatePost;
import com.rescue.rescue.request.UpdatePost;
import com.rescue.rescue.service.Post.PostService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("${api.prefix}/posts")
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/allPost")
    public ResponseEntity<ApiResponse> getMethodName(@RequestParam  (value="cursor", required = true, defaultValue = "0") Long cursor,
                                                     @RequestParam(value = "limit", required = true, defaultValue = "10") Integer limit) {
        List<PostDto> posts = postService.getAllPost(cursor, limit);
        return ResponseEntity.ok(new ApiResponse("success", posts));
    }


    @PostMapping("/createPost")
    public ResponseEntity<ApiResponse> postMethodName(@RequestBody CreatePost post) {
        //TODO: process POST request
        PostDto createdPost = postService.createPost(post);
        return ResponseEntity.ok(new ApiResponse("success", createdPost));
    }

    @PutMapping("/UpdatePost")
    public ResponseEntity<ApiResponse> putMethodName(@RequestBody UpdatePost post) {
        //TODO: process PUT request
        postService.updatePost(post);
        return ResponseEntity.ok(new ApiResponse("success", null));
    }
    
    

}
