package com.bilgeadam.controller;

import com.bilgeadam.dto.request.ChangePasswordDto;
import com.bilgeadam.dto.request.NewCreateUserRequestDto;
import com.bilgeadam.dto.response.UserProfileChangePasswordResponseDto;
import com.bilgeadam.dto.request.UserProfileUpdateRequestDto;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.bilgeadam.constant.ApiUrls.*;
import static com.bilgeadam.constant.ApiUrls.FORGOT_PASSWORD;

@RestController
@RequestMapping(USER_PROFILE)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping(CREATE)
    public ResponseEntity<Boolean> createUser(@RequestBody NewCreateUserRequestDto dto){
        return ResponseEntity.ok(userProfileService.createUser(dto));
    }

    @GetMapping(FIND_ALL)
    @PreAuthorize("hasAnyAuthority('USER')")
    public ResponseEntity<List<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @GetMapping(ACTIVATE_STATUS + "/{authId}")
    public ResponseEntity<Boolean> activateStatus(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.activateStatus(authId));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<UserProfile> update(@RequestBody UserProfileUpdateRequestDto dto){
        return ResponseEntity.ok(userProfileService.update(dto));
    }

    @DeleteMapping(DELETE_BY_ID + "/{authId}")
    public ResponseEntity<Boolean> delete(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.delete(authId));
    }

    @GetMapping("find-by-username/{username}")
    public ResponseEntity<UserProfile> activateStatus(@PathVariable String username){
        return ResponseEntity.ok(userProfileService.findByUsernameIgnoreCase(username));
    }

    @GetMapping("find-all-with-cache")
    public ResponseEntity<List<UserProfile>> findAllWithCache(){
        return ResponseEntity.ok(userProfileService.findAllWithCache());
    }

    @GetMapping("/find-by-role/{role}")
    public ResponseEntity<List<UserProfile>> findByRole(@PathVariable String role){
        return ResponseEntity.ok(userProfileService.findByRole(role));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Boolean> findByRole(@RequestBody ChangePasswordDto dto){
        return ResponseEntity.ok(userProfileService.changePassword(dto));
    }
    @PutMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> updatePassword(@RequestBody UserProfileChangePasswordResponseDto dto){
        return ResponseEntity.ok(userProfileService.updatePassword(dto));
    }

    @GetMapping("/find-by-auth-id/{authId}")
    public ResponseEntity<Optional<UserProfile>> findByAuthId(@PathVariable Long authId){
        return ResponseEntity.ok(userProfileService.findByAuthId(authId));
    }

}
