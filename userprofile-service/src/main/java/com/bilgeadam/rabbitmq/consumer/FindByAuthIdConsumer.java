package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.exception.ErrorType;
import com.bilgeadam.exception.UserProfileManagerException;
import com.bilgeadam.mapper.IUserProfileMapper;
import com.bilgeadam.rabbitmq.model.AuthIdModel;
import com.bilgeadam.repository.entity.UserProfile;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FindByAuthIdConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = "queue-create-post")
    public Object findByAuthId(AuthIdModel model){
        System.out.println(model);
        Optional<UserProfile> userProfileOptional =  userProfileService.findByAuthId(model.getAuthId());
        if(userProfileOptional.isEmpty())
            throw new UserProfileManagerException(ErrorType.USER_NOT_FOUND);
        return IUserProfileMapper.INSTANCE.fromUserProfileToUserProfileModel(userProfileOptional.get());
    }
}
