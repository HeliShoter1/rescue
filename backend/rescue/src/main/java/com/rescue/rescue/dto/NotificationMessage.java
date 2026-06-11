package com.rescue.rescue.dto;

import lombok.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationMessage implements Serializable {
    private Long receiverId;
    private Long senderId;
    private String title;
    private String content;
}