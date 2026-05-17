package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rescue.rescue.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
}
