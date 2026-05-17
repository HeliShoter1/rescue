package com.rescue.rescue.request;

import com.rescue.rescue.enums.PostStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UpdatePost {
    private Long id;
    private String content;
    private PostStatus status;
}
