package com.rescue.rescue.dto;

import com.rescue.rescue.model.History;
import com.rescue.rescue.model.Message;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;   
import lombok.Setter;

@Setter
@Getter
@Data
@Builder
public class MessageDto {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private String status;
    private String createAt;


    public static MessageDto fromEntity(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .content(message.getContent())
                .status(message.getStatus().toString())
                .createAt(message.getCreateAt().toString())
                .build();
    }
}
