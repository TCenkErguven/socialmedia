package com.bilgeadam.config.security;


import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final UserProfileService userProfileService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUserId(String role) throws UsernameNotFoundException {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
        return User.builder()
                    .username(role)
                    .password("")
                    .accountExpired(false)
                    .accountLocked(false)
                    .authorities(grantedAuthority)
                    .build();
    }

}
