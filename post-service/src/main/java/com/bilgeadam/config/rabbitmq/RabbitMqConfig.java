package com.bilgeadam.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    //create post producer işlemi için gerekli değişkenler
    String directExchange = "exchange-create-post";
    String registerBindingKey = "binding-create-post";
    String queueCreatePost = "queue-create-post";
    @Bean
    DirectExchange exchangeUser(){return new DirectExchange(directExchange);}
    @Bean
    Queue createPostQueue(){return new Queue(queueCreatePost);}

    @Bean
    public Binding bindingCreatePost(final Queue createPostQueue, final DirectExchange exchangeUser){
        return BindingBuilder.bind(createPostQueue).to(exchangeUser).with(registerBindingKey);
    }

}
