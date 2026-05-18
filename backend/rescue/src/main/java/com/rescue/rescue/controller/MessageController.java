package com.rescue.rescue.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rescue.rescue.enums.MessageStatus;
import com.rescue.rescue.reponse.ApiResponse;
import com.rescue.rescue.request.MessageSend;
import com.rescue.rescue.service.Message.IMessageService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("${api.prefix}/messages")
@AllArgsConstructor
public class MessageController {
    private final IMessageService messageService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> getMethodName(@RequestBody MessageSend request) {
        messageService.sendMessage(request.getSenderId(), request.getReceiverId(), request.getContent());
        return ResponseEntity.ok(new ApiResponse("Message sent", null));
    }

    @GetMapping("/all-message")
    public ResponseEntity<ApiResponse> getMethodName(@RequestParam MessageStatus status) {
        // TODO: Implement logic to fetch all messages
        messageService.getMessageByUserID(status); // Example call, replace with actual parameters
        return ResponseEntity.ok(new ApiResponse("All messages retrieved", null));
    }
    
    
    
}
