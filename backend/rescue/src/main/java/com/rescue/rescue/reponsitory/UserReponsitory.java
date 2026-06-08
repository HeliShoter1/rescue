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
import com.rescue.rescue.model.Place;
import com.rescue.rescue.model.User;


public interface UserReponsitory extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findById(Long UserId);

    @Query("""
        SELECT u 
        FROM User u 
        WHERE (:status IS NULL OR u.status = :status) 
        AND (:role IS NULL OR u.role = :role)
        AND (:search IS NULL OR u.name LIKE %:search% OR u.phoneNumber LIKE %:search%)
        AND u.id >= :cursor 
        ORDER BY u.id ASC
        LIMIT :limit
    """)
    List<User> findByFilter(
        @Param("status") UserStatus status,
        @Param("role") UserRole role,
        @Param("search") String search,
        @Param("cursor") Long cursor,
        @Param("limit") Integer limit);

    @Modifying
    @Query("UPDATE User u SET u.place = null WHERE u.id = :id")
    void clearPlaceById(@Param("id") Long id);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
    void updateStatusById(@Param("id") Long id, @Param("status") UserStatus status);

    @Modifying
    @Query("UPDATE User u SET u.place = :place WHERE u.id = :id")
    void updatePlaceById(@Param("id") Long id, @Param("place") Place place);
}
