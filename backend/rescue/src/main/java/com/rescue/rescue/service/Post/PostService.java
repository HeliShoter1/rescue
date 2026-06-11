package com.rescue.rescue.service.Post;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rescue.rescue.dto.PostDto;
import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.exceptions.AccessDeniedException;
import com.rescue.rescue.exceptions.ResourceNotFoundException;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.Post;
import com.rescue.rescue.model.RescueTeam;
import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.GroupRepository;
import com.rescue.rescue.reponsitory.PostRepository;
import com.rescue.rescue.reponsitory.RescueTeamRepository;
import com.rescue.rescue.reponsitory.UserReponsitory;
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
    private final UserReponsitory userReponsitory;


    @Override
    public List<PostDto> getAllPost(Long cursor, Integer limit) {
        List<PostDto> posts = postRepository.getAllPost(cursor, limit).stream().map(PostDto::fromEntity).toList();
        return posts;
    }

    @Override
    public PostDto createPost(CreatePost post) {    
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        Post postEntity = modelMapper.map(post, Post.class);
        User user = userReponsitory.findById(userId).get();
        postEntity.setUser(user);
        Post savedPost = postRepository.save(postEntity);
        return PostDto.fromEntity(savedPost);
    }

    @Override
    public PostDto updatePost(UpdatePost post) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((RescueUserDetail) authentication.getPrincipal()).getId();
        UserRole role =  ((RescueUserDetail) authentication.getPrincipal()).getRole();
        System.out.println(userId);

        Post postEntity = postRepository.findById(post.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + post.getId()));

        boolean isOwner   = userId.equals(postEntity.getUser().getId());
        System.out.println(isOwner);
        boolean isAdmin   = role == UserRole.ADMIN;

        if(isAdmin || isOwner){
            modelMapper.map(post, postEntity);
            PostDto postDto = PostDto.fromEntity(postRepository.save(postEntity));  
            return postDto; 
        }else{
            RescueTeam rescueTeam = rescueTeamRepository.findByPostId(post.getId()).orElseThrow(() -> new AccessDeniedException("You are not manager of post to update this post"));
        
            User user = groupRepository
                        .findByUserIdAndRescueTeamId(userId, rescueTeam.getId())
                        .map(Group::getUser)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));;
            UserRole userRole = (user != null) ? user.getRole() : null;

            boolean isManager = userRole == UserRole.MANAGER;

            if (!isManager) {
                throw new AccessDeniedException("You are not manager of post to update this post");
            }

            PostDto postDto = PostDto.fromEntity(postRepository.save(postEntity));  
            return postDto; 
        }
    }
}