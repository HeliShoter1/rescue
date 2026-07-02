package com.rescue.rescue.reponsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.rescue.rescue.enums.TaskStatus;
import com.rescue.rescue.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
        SELECT t 
        FROM Task t 
        WHERE t.rescueTeam.id = :rescueTeamId
        ORDer BY t.id ASC , t.createAt DESC
    """)
    List<Task> findByRescueTeamId(Long rescueTeamId);

    @Query("""
        SELECT t 
        FROM Task t 
        WHERE t.user.id = :userId
        ORDer BY t.id ASC , t.createAt DESC , t.updateAt DESC
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
    SET t.user.id = :userId, t.status = com.rescue.rescue.enums.TaskStatus.AGIND
    WHERE t.id = :taskId
    """)
    void RegisterTask(Long userId, Long taskId);
    
    @Query(value = """
        SELECT AVG(EXTRACT(EPOCH FROM (t.complete_at - t.create_at)) / 60)
        FROM task t
        WHERE t.status = 'COMPLETED' AND t.complete_at IS NOT NULL
        """, nativeQuery = true)
    Double findAvgCompletionMinutesNative();

    @Query("""
        SELECT COUNT(t)
        FROM Task t
        WHERE t.status = :status
            """)
    Integer countByStatus(TaskStatus status);

}
