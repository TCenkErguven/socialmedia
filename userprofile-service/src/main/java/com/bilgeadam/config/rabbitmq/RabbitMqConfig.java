package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${rabbitmq.exchange-user}")
    String exchange;

    @Value("${rabbitmq.queueRegister}")
    String queueNameRegister;
    @Bean
    Queue registerQueue(){
        return new Queue(queueNameRegister);
    }

    @Bean
    DirectExchange exchangeUser(){
        return new DirectExchange(exchange);
    }

    //Save işlemi Elastic
    @Value("${rabbitmq.registerElasticQueue}")
    String registerElasticQueue;
    @Value("${rabbitmq.registerElasticBindingKey}")
    String registerElasticBindingKey;

    @Bean
    Queue registerElasticQueue(){
        return new Queue(registerElasticQueue);
    }

    @Bean
    public Binding bindingRegisterElastic(final Queue registerElasticQueue,final DirectExchange exchangeUser){
        return BindingBuilder.bind(registerElasticQueue).to(exchangeUser).with(registerElasticBindingKey);
    }

    String queueCreatePost = "queue-create-post";
    @Bean
    Queue createPostQueue(){return new Queue(queueCreatePost);}


}
