package com.rescue.rescue.service.Message;

public interface IMessageService {
    public void sendMessage(Long senderId, Long receiverId, String content);
}
