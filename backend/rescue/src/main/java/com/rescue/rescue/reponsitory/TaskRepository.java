package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
        SELECT t 
        FROM Task t 
        WHERE t.rescueTeam.id = :rescueTeamId
        ORDer BY t.id ASC and t.createAt DESC
    """)
    List<Task> findByRescueTeamId(Long rescueTeamId);

    @Query("""
        SELECT t 
        FROM Task t 
        WHERE t.user.id = :userId
        ORDer BY t.id ASC and t.createAt DESC and t.updateAt DESC
    """)
    List<Task> findByUserId(Long userId);


    @Modifying
    @Query("""
        UPDATE Task t 
        SET t.status = :status 
        WHERE t.id = :taskId
    """)
    void updateStatusById(Long taskId, String status);

    @Modifying
    @Query("""
        UPDATE Task t 
        SET t.user.id = :userId 
        WHERE t.id = :taskId
    """)
    Task RegisterTask(Long userId, Long taskId);
    
    
}
