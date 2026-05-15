package com.rescue.rescue.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rescue.rescue.enums.UserRole;
import com.rescue.rescue.enums.UserStatus;
import com.rescue.rescue.model.User;

public interface UserReponsitory extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Query("""
        SELECT u 
        FROM User u 
        WHERE (:status IS NULL OR u.status = :status) 
        AND (:role IS NULL OR u.role = :role)
        AND id >= :cursor 
        order by id asc
        limit :limit
    """)
    List<User> findByFilter(
        @Param("status") UserStatus status,
        @Param("role") UserRole role, 
        @Param("cursor") Long cursor, 
        @Param("limit") Integer limit);
}
