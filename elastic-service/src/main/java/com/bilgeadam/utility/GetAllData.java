package com.bilgeadam.utility;

import com.bilgeadam.manager.IUserManager;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class GetAllData {
    private final IUserManager userManager;
    private final UserProfileService userProfileService;


    //@PostConstruct
    public void initData(){
        Iterable<UserProfile> userList = (userManager.findAll().getBody());
        userProfileService.saveAll(userList);
    }
}
