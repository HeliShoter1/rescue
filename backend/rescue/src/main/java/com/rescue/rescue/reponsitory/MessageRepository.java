package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.enums.MessageStatus;
import com.rescue.rescue.model.Message;

import io.lettuce.core.dynamic.annotation.Param;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Modifying
    @Query("UPDATE Message m SET m.status = :status WHERE m.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") MessageStatus status);

    @Query("SELECT m FROM Message m WHERE m.receiver.id = :receiverId AND m.status = :status")
    List<Message> findByReceiverIdAndStatus(@Param("receiverId") Long receiverId, @Param("status") MessageStatus status);
    
}
