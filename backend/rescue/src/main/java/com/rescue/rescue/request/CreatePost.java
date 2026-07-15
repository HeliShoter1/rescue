package com.rescue.rescue.request;

import com.rescue.rescue.enums.TypePost;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreatePost {
    private String content;
    private TypePost typePost;
}
