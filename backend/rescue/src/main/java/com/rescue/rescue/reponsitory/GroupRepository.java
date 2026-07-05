// thư viện: spring-data-jpa, org.springframework.data.repository.query.Param
package com.rescue.rescue.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rescue.rescue.enums.MemberStatus;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.User;

public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByUserIdAndRescueTeamId(Long userId, Long rescueTeamId);

    @Query("SELECT g.user FROM Group g WHERE g.rescueTeam.id = :rescueTeamId AND g.id > :cursor ORDER BY g.id ASC LIMIT :limit")
    List<User> findUsersByRescueTeamId(
        @Param("rescueTeamId") Long rescueTeamId,
        @Param("cursor") Long cursor,
        @Param("limit") Integer limit
    );

    @Query("select g.user from Group g join User u on g.user.id = u.id where u.role = 'MANAGER' and g.rescueTeam.id = :rescueTeamId")
    List<User> findManagerByRescueTeamId(@Param("rescueTeamId") Long rescueTeamId);

    @Query("select g from Group g where g.user.id = :userId")
    Group findMemberByUserId(Long userId);

    @Query("select g from Group g where g.rescueTeam.id = :rescueTeamId and g.status = :status and g.id > :cursor order by g.id asc limit :limit")
    List<Group> findByRescueTeamIdAndStatus(
        @Param("rescueTeamId") Long rescueTeamId,
        @Param("status") MemberStatus status,
        @Param("cursor") Long cursor,
        @Param("limit") Integer limit
    );

    @Query("select g from Group g where g.rescueTeam.post.id = :postId and g.status = :status and g.id > :cursor order by g.id asc limit :limit")
    List<Group> findByPostIdAndStatus(
        @Param("postId") Long postId,
        @Param("status") MemberStatus status,
        @Param("cursor") Long cursor,
        @Param("limit") Integer limit
    );

    @Modifying
    @Query("""
            Update Group g set g.status = :status where g.user.id = :userId and g.rescueTeam.id = :rescueTeamId
            """)
    void updateMemberStatus(
        @Param("userId") Long userId,
        @Param("rescueTeamId") Long rescueTeamId,
        @Param("status") MemberStatus status
    );
}