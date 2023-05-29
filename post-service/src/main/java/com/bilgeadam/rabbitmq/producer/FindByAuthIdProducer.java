package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.AuthIdModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByAuthIdProducer {

    private String directExchange = "exchange-create-post";
    private String userBindingKey = "binding-create-post";

    private final RabbitTemplate rabbitTemplate;

    public Object findByAuthIdRabbit(AuthIdModel model){
        return rabbitTemplate.convertSendAndReceive(directExchange,userBindingKey,model);
    }
}
