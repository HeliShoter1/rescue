package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.model.History;

public interface HistoryRepository extends JpaRepository<History, Long> {
    @Query("SELECT h FROM History h WHERE h.id < :cursor ORDER BY h.id DESC")
    List<History> GetAllByAdmin(Long cursor, Integer limit);

    @Query("SELECT h FROM History h WHERE h.post.user.userID = :userId AND h.id < :cursor ORDER BY h.id DESC")
    List<History> GetAllByUserId(Long userId, Long cursor, Integer limit);
}
