package com.bilgeadam.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity  //has Role'u aşağıdaki kullanmak için yazıyoruz. '.hasRole()' metodunun çalışabilmesi için kullanılır
@EnableGlobalMethodSecurity(prePostEnabled = true) // metotları işaretlerek role kontrolü yapmaya yarar @PreAuthrorize 'la birlikte
public class SecurityConfig {



    @Bean
    JwtFilter getJwtFilter(){
        return new JwtFilter();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**")
                        .permitAll().anyRequest().authenticated();
        httpSecurity.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
