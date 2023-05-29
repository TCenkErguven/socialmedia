package com.bilgeadam.rabbitmq.producer;

import com.bilgeadam.rabbitmq.model.RegisterElasticModel;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterProducer {
    @Value("${rabbitmq.exchange-user}")
    private String directExchange;
    @Value("${rabbitmq.registerElasticBindingKey}")
    private String registerElasticBindingKey;

    private final RabbitTemplate rabbitTemplate;

    public void registerElastic(RegisterElasticModel model){
        rabbitTemplate.convertAndSend(directExchange,registerElasticBindingKey,model);
    }

}
