package com.rescue.rescue.dto;

import lombok.Data;
import lombok.Getter;   
import lombok.Setter;

public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String status;
    private String createAt;
}