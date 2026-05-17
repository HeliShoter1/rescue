package com.rescue.rescue.service.Post;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PostDto;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.GroupRepository;
import com.rescue.rescue.reponsitory.PostRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.request.CreatePost;
import com.rescue.rescue.request.UpdatePost;
import com.rescue.rescue.sercurity.user.RescueUserDetail;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PostService implements IPostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final RescueTeamRepository rescueTeamRepository;
    private final GroupRepository groupRepository;


    @Override
    public List<PostDto> getAllPost(Long cursor, Integer limit) {
        List<PostDto> posts = postRepository.getAllPost(cursor, limit).stream().map(PostDto::fromEntity).toList();
        return posts;
    }

    @Override
    public PostDto createPost(CreatePost post) {    
        Post postEntity = modelMapper.map(post, Post.class);
        Post savedPost = postRepository.save(postEntity);
        return PostDto.fromEntity(savedPost);
    }

    @Override
    public void updatePost(UpdatePost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();

        Post postEntity = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + post.getId()));

        RescueTeam rescueTeam = rescueTeamRepository.findByPostId(post.getId());
        User group = groupRepository.findByUserIdAndRescueTeamId(userId, rescueTeam.getId());
        UserRole userRole = (group != null) ? group.getRole() : null;

        boolean isOwner   = userId.equals(postEntity.getUser().getId());
        boolean isAdmin   = postEntity.getUser().getRole() == UserRole.ADMIN;
        boolean isManager = userRole == UserRole.MANAGER;

        if (!isOwner && !isAdmin && !isManager) {
            throw new RuntimeException("You are not authorized to update this post");
        }

        modelMapper.map(post, postEntity);
        postRepository.save(postEntity); 
    }
}