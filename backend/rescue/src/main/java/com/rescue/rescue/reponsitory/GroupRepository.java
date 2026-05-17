package com.rescue.rescue.reponsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import com.rescue.rescue.model.Group;
import com.rescue.rescue.model.User;

public interface GroupRepository extends JpaRepository<Group, Long> {

    User findByUserIdAndRescueTeamId(Long user_id, Long id);
    
}
