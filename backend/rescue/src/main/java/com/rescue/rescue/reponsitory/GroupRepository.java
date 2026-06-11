// thư viện: spring-data-jpa, org.springframework.data.repository.query.Param
package com.rescue.rescue.reponsitory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    User findManagerByRescueTeamId(@Param("rescueTeamId") Long rescueTeamId);
}