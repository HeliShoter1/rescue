package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rescue.rescue.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    
}
