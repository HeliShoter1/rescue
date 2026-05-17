package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rescue.rescue.model.RescueTeam;

import jakarta.transaction.Transactional;

@Transactional
public interface RescueTeamRepository extends JpaRepository<RescueTeam, Long> {
    RescueTeam findByPostId(Long postId);
    
}