package com.bilgeadam.controller;

import static com.bilgeadam.constant.ApiUrls.*;

import com.bilgeadam.dto.request.*;
import com.bilgeadam.dto.response.RegisterResponseDto;
import com.bilgeadam.repository.entity.Auth;
import com.bilgeadam.repository.entity.enums.ERole;
import com.bilgeadam.service.AuthService;
import com.bilgeadam.utility.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.imageio.plugins.tiff.GeoTIFFTagSet;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AUTH)
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping(REGISTER + "Md5")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerMd5(dto));
    }

    @PostMapping(REGISTER+"2") //register2
    public ResponseEntity<RegisterResponseDto> registerWithRabbitMq(@RequestBody @Valid RegisterRequestDto dto){
        return ResponseEntity.ok(authService.registerWithRabbitMq(dto));
    }
    @PostMapping(ACTIVATE_STATUS)
    public ResponseEntity<Boolean> activateStatus(@RequestBody ActivateRequestDto dto){
        return ResponseEntity.ok(authService.activateStatus(dto));
    }
    @PostMapping(LOGIN)
    @Operation(summary = "#1") // endpointe açıklama yapmak için kullanılır
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.login(dto));
    }
    @PostMapping(LOGIN +"Md5")
    public ResponseEntity<String> loginWithMd5(@RequestBody LoginRequestDto dto){
        return ResponseEntity.ok(authService.loginMD5(dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<Auth>> findAll(){
        return ResponseEntity.ok(authService.findAll());
    }

    @Hidden
    @DeleteMapping(DELETE_BY_ID)
    public ResponseEntity<Boolean> delete(String token){
        return ResponseEntity.ok(authService.delete(token));
    }

    @PutMapping("/update-username-email")
    public ResponseEntity<Boolean> update(@RequestBody UpdateEmailOrUsernameRequestDto dto){
        return ResponseEntity.ok(authService.update(dto));
    }

    @GetMapping("/create-token-with-id")
    public ResponseEntity<String> createToken(Long id){
        return ResponseEntity.ok(tokenProvider.createToken(id).get());
    }

    @GetMapping("/create-token-with-role")
    public ResponseEntity<String> createToken(Long id, ERole role){
        return ResponseEntity.ok(tokenProvider.createToken(id, role).get());
    }

    @GetMapping("/get-id-from-token")
    public ResponseEntity<Long> getIdFromToken(String token){
        return ResponseEntity.ok(tokenProvider.getIdFromToken(token).get());
    }

    @GetMapping("/get-role-from-token")
    public ResponseEntity<String> getRoleFromToken(String token){
        return ResponseEntity.ok(tokenProvider.getRoleFromToken(token).get());
    }

    @GetMapping("/redis")
    @Cacheable(value = "redisexample")
    public String redisExample(String value){
        try{
            Thread.sleep(3000);
            return value;
        }catch (Exception e){
            throw new RuntimeException();
        }
    }

    @GetMapping("/cache-delete")
    @CacheEvict(cacheNames = "redisexample",allEntries = true)
    public void cacheDelete(){

    }

    @GetMapping("/find-by-role/{role}")
    public ResponseEntity<List<Long>> findByRole(@PathVariable String role){
        return ResponseEntity.ok(authService.findByRole(role));
    }

    @PutMapping(PASSWORD_CHANGE)
    public ResponseEntity<Boolean> changePassword(@RequestBody ToAuthPasswordChangeDto dto){
        return ResponseEntity.ok(authService.changePassword(dto));
    }

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> forgotPassword(String email,String username){
        return ResponseEntity.ok(authService.forgotPassword(email,username));
    }


}
