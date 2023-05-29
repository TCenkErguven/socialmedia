package com.bilgeadam.manager;

import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.request.UserProfileChangePasswordRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.bilgeadam.constant.ApiUrls.DELETE_BY_ID;
import static com.bilgeadam.constant.ApiUrls.FORGOT_PASSWORD;

@FeignClient(url = "http://localhost:8080/api/v1/user-profile", name = "auth-userprofile")
public interface IUserProfileManager {
    @PostMapping("/create")
    public ResponseEntity<Boolean> createUser(@RequestHeader(value = "Authorization") String token
            ,@RequestBody NewCreateUserRequestDto dto);

    @GetMapping("/activate-status/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId);

    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId);

    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> updatePassword(@RequestBody UserProfileChangePasswordRequestDto dto);
}
