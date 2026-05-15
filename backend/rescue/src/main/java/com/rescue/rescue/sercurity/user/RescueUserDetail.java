package com.rescue.rescue.sercurity.user;

import com.rescue.rescue.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RescueUserDetail implements UserDetails{
    private Long id;
    private String phoneNumber;
    private String password;

    private Collection<GrantedAuthority> authorities;

    public static RescueUserDetail buildUserDetails(User user) {
        List<GrantedAuthority> authorities = user.getRole().name().lines()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return new RescueUserDetail(
                user.getId(),
                user.getPhoneNumber(),
                user.getPassword(),
                authorities);
    }

    public Long getId(){
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }
}
