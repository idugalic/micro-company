package com.idugalic.apigateway.configuration;

import org.axonframework.spring.config.AnnotationDriven;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AnnotationDriven
public class RabbitConfiguration {

    @Value("${spring.application.queue}")
    private String queueName;

    @Value("${spring.application.blog.exchange}")
    private String blogExchangeName;

    @Value("${spring.application.project.exchange}")
    private String projectExchangeName;

    @Bean
    Queue eventStream() {
        return new Queue(queueName, true);
    }

    @Bean
    FanoutExchange blogEventBusExchange() {
        return new FanoutExchange(blogExchangeName, true, false);
    }

    @Bean
    Binding blogBinding() {
        return new Binding(queueName, Binding.DestinationType.QUEUE, blogExchangeName, "*.*", null);
    }

    @Bean
    FanoutExchange projectEventBusExchange() {
        return new FanoutExchange(projectExchangeName, true, false);
    }

    @Bean
    Binding projectBinding() {
        return new Binding(queueName, Binding.DestinationType.QUEUE, projectExchangeName, "*.*", null);
    }

   
}
