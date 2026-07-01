package com.rescue.rescue.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rescue.rescue.model.Relative;

public interface RelativeReponsitory extends JpaRepository<Relative, Long>  {
    
    @Query("""
        SELECT r 
        FROM Relative r 
        WHERE r.user.id = :userId
        AND r.id >= :cursor
        ORDER BY r.id ASC
        LIMIT :limit
    """)
    List<Relative> findByUserId(Long userId, Long cursor, Integer limit);

    @Query
    ("""
        SELECT r 
        FROM Relative r 
        WHERE r.relative.id = :relativeId
        AND r.user.id = :userId
    """)
    Optional<Relative> findByRelativeIdAndUserId(@Param("relativeId") Long relativeId, @Param("userId") Long userId);

}   
