package com.rescue.rescue.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class MessageSend {
    private Long senderId;
    private Long receiverId;
    private String content;
    
}
