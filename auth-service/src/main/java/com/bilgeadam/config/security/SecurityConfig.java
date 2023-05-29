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
    //SecurityFilterChain --> Gelen istekleri işletmek ve filtreleyerek bir zincir oluşturmayı sağlar
    //HttpSecurity --> Http işlemlerinde ki güvenlikten sorumludur. Kimlik doğrulama veya session işlemlerinde
    //hangi kişinin endpointe erişip erişemeyeceğini belirler. Rollere göre kontrol yapar.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
//cors --> http'nin oluşturabileceği güvenlik açıklarını gidermek için browserların kullandığı bir erişim kısıtlama prensibidir.
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/api/v1/auth/registerMd5",
                        "/api/v1/auth/register2",
                        "/api/v1/auth/login",
                        "/api/v1/auth/loginMd5",
                        "/api/v1/auth/forgot-password",
                        "/api/v1/auth/activate-status")
                        .permitAll().anyRequest().authenticated();//swagger endpointine herkes ulaşabilir.
//                .antMatchers("/api/v1/auth/register").authenticated() //giriş yapan kişi (rolü önemli değil) update yapabilir.
//                .antMatchers("/api/v1/auth/find-all").hasAnyRole("ADMIN","USER") //iki rolün birden ulaşabildiği
//                .anyRequest().hasRole("ADMIN"); //diğer bütün işlemleri ADMIN rolü yapabilir
        //JwtFilterdan gelen bilgilere göre burada endpointler için son olarak bir filtreleme daha yapılır.
        httpSecurity.addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
