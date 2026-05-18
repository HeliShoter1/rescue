package com.rescue.rescue.dto;

// thư viện: lombok, jackson
import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEvent implements Serializable {
    private Long messageId;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime createAt;
}