package com.rescue.rescue.reponsitory;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rescue.rescue.model.User;

public interface UserReponsitory extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);


}
