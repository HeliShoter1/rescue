package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.enums.RescueTeamStatus;
import com.rescue.rescue.model.RescueTeam;

import jakarta.transaction.Transactional;

@Transactional
public interface RescueTeamRepository extends JpaRepository<RescueTeam, Long> {
    RescueTeam findByPostId(Long postId);

    @Query("""
        SELECT r 
        FROM RescueTeam r 
        WHERE r.status = :status AND r.id >= :cursor 
        ORDER BY r.id ASC
        limit :limit
    """)
    List<RescueTeam> findByStatus(RescueTeamStatus status, Long cursor, Integer limit);
    
    
}