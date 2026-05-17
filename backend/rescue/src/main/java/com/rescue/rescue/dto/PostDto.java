package com.rescue.rescue.dto;

import java.time.LocalDate;

import com.rescue.rescue.enums.PostStatus;
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.Post;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PostDto {
    private Long id;
    private UserDto user;
    private LocalDate createAt;
    private PostStatus status;
    private LocalDate updateAt;
    private String content;

        public static PostDto fromEntity(Post post) {
            return PostDto.builder()
                    .id(post.getId())
                    .user(UserDto.fromEntity(post.getUser()))
                    .createAt(post.getCreateAt())
                    .status(post.getStatus())
                    .updateAt(post.getUpdateAt())
                    .content(post.getContent())
                    .build();
        }
}

