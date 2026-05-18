package com.rescue.rescue.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class MessageSend {
    private Long sender_id;
    private Long receiver_id;
    private String content;
    
}
