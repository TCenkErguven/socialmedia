package com.bilgeadam.config.security;

import com.bilgeadam.exception.AuthManagerException;
import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.utility.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

//OncePerRequestFilter --> Her bir istek için bir kere çalışarak bir filtreleme işlemi sağlıyor (Spring Framework sınıfıdır.)

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtUserDetails jwtUserDetails;

    /**
     * HttpServlet --> WebFlax, OpenFeign, HttpClient, RestTemplate
     * HttpServlet, Java Servlet API tarafından sağlanan bir sınıftır ve HTTP protokolü üzerinden gelen istekleri işlemek için kullanılır.
     * Servlet, dinamik web uygulamaları geliştirmek için kullanılır ve doğrudan "web sunucusunda" çalışır.
     * HTTP istekleri(GET, POST, DELETE, UPDATE, OPTIONS, vb.)
     *
     * HttpServletRequest --> Bir HTTP isteğinin sorgunu ilgili sunucuya gönderir. İstemci tarafından sunucuya gönderilen isteği içerir.
     *                        Örn; istek başlıkları, parametreler, request yolu, HTTP yöntemi
     * HttpServletResponse --> Sunucudan gelen cevabı yakalıyor.
     *
     * FilterChain -->  İsteği işlemek ve yanıt üretmek için bir dizi filtreyi içeren bir yapıdır.
     *                  Bir HTTP isteği ServletRequest'e geldiğinde burada bir filtreleme yapılır ve sunucuya bu şekilde gönderilir.
     */


    /**
     *
     * @param request           İçerisinde Header bilgisi dolu birşekilde gelecektir.
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeaderToken = request.getHeader("Authorization");
        System.out.println("Header'dan gelen token bilgisi ---> " + authHeaderToken);

        if(authHeaderToken != null && authHeaderToken.startsWith("Bearer ")){
            String token = authHeaderToken.substring(7);
            Optional<Long> authId = jwtTokenProvider.getIdFromToken(token);
            if(authId.isPresent()){
                //kullanıcının göndermiş olduğu token bilgisinin role ve kimlik bilgilerinin doğrulanması
                UserDetails userDetails = jwtUserDetails.loadUserByUsername(authId.get());
                System.out.println("UserDetails --> " + userDetails);
                //Kimlik doğrulama token'ını (authenticationToken) oluşturur.
                //Bu token, kimlik doğrulama işlemlerinde kullanılır.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                throw new AuthManagerException(ErrorType.INVALID_TOKEN);
            }
        }
        //Filtre zincirindeki bir sonraki filtreye veya hedef kaynağa isteği iletmek için filterChan'i kullanır.
        //Bu, filtreleme işlemini tamamlar ve sonucunda isteğin devam etmesini sağlar.
        filterChain.doFilter(request,response);
    }
}
