package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rescue.rescue.model.Notification;

public interface NotificationsRepository extends JpaRepository<Notification, Long> {
    @Query("""
        SELECT n 
        FROM Notification n 
        WHERE n.user.id = :userId
        AND n.id >= :cursor
        ORDER BY n.id ASC
        LIMIT :limit
    """)
    List<Notification> findByUserId(Long userId, Long cursor, Integer limit);
    @Modifying
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    Notification updateStatus(@Param("id") Long id, @Param("status") Boolean status);
}
