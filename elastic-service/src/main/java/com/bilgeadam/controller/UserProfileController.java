package com.bilgeadam.controller;


import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static com.bilgeadam.constant.ApiUrls.*;



@RestController
@RequestMapping(ELASTIC)
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/userprofile/find-all")
    public ResponseEntity<Iterable<UserProfile>> findAll(){
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @DeleteMapping("/userprofile/delete-all")
    public ResponseEntity<Void> deleteAll(){
        userProfileService.deleteAll();
        return ResponseEntity.ok().build();
    }
    @GetMapping(FIND_ALL +"-pageable")
    public ResponseEntity<Page<UserProfile>> findAll(int page,int size,String sortParameter, String sortType){
        return ResponseEntity.ok(userProfileService.findAll(page,size,sortParameter,sortType));
    }
}
