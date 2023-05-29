package com.bilgeadam.rabbitmq.consumer;

import com.bilgeadam.mapper.IUserProfileMapper;
import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import com.bilgeadam.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterConsumer {
    private final UserProfileService userProfileService;

    @RabbitListener(queues = ("${rabbitmq.registerElasticQueue}"))
    public void registerElastic(RegisterElasticModel model){
        userProfileService.save(IUserProfileMapper.INSTANCE.fromRegisterElasticModelToUserProfile(model));
    }
}
