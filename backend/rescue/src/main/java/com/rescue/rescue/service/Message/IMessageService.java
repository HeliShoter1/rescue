package com.rescue.rescue.service.Message;

import java.util.List;

import com.rescue.rescue.dto.MessageDto;
import com.rescue.rescue.enums.MessageStatus;

public interface IMessageService {
    public void sendMessage(Long senderId, Long receiverId, String content);
    public List<MessageDto> getMessageByUserID( MessageStatus status);

}
