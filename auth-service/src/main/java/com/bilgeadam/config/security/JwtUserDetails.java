package com.bilgeadam.config.security;

import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetails implements UserDetailsService {
    private final AuthService authService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public UserDetails loadUserByUsername(Long id) throws UsernameNotFoundException {
        Optional<Auth> optionalAuth = authService.findById(id);
        if(optionalAuth.isPresent()){
            /**
             * Rolleri normalde liste şeklinde tutarız ama bu uygulamada tek bir rolü varmış gibi tuttuk.
             * Eğer liste olarak tutarsak ==>
             * List<GrantedAuthority>authorityList = new ArrayList<>();
             * Auth authRole = authService.findById(id);
             * authRole.getRoles().foreach(roles -> {
             *     authorityList.add(new SimpleGrantedAuthority(roles));
             * })
             */
            //SimpleGrantedAuthority kullanıcının tek bir rolü olduğunu varsayar
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(optionalAuth.get().getRole().toString()); //USER,ADMIN

            return User.builder()
                    .username(optionalAuth.get().getUsername())
                    //uygulamada şifte doğrulama işlemi yapılmayacağı için boş geçilir.
                    .password("")
                    //kullanıcının hesabının süresinin dolmadığını belirtir.
                    .accountExpired(false)
                    .accountLocked(false)
                    //kullanıcının her bir işlem için buradaki rol bilgisi karşılaştırılır
                    .authorities(grantedAuthority)
                    .build();
        }
        return null;
    }
}
