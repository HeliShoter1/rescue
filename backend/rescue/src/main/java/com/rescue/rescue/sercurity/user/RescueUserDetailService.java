package com.rescue.rescue.sercurity.user;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rescue.rescue.model.User;
import com.rescue.rescue.reponsitory.UserReponsitory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RescueUserDetailService implements UserDetailsService {
    private final UserReponsitory userReponsitory;

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        User user = userReponsitory.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with phone number: " + phoneNumber));
        return RescueUserDetail.buildUserDetails(user);
    }
}