package com.bilgeadam.manager;

import com.bilgeadam.repository.entity.UserProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import static com.bilgeadam.constant.ApiUrls.FIND_ALL;

@FeignClient(
        name = "elastic-userprofile",
        url = "http://localhost:8080/api/v1/user-profile"
)
public interface IUserManager {
    @GetMapping(FIND_ALL)
    public ResponseEntity<List<UserProfile>> findAll();
}
