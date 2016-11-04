package com.idugalic.commandside.project.configuration;

import org.axonframework.amqp.eventhandling.spring.SpringAMQPPublisher;
import org.axonframework.eventhandling.EventBus;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.application.exchange}")
    private String exchangeName;

    @Value("${spring.application.queue}")
    private String queueName;

    @Bean(initMethod = "start")
    public SpringAMQPPublisher amqpBridge(EventBus eventBus) {
        return new SpringAMQPPublisher(eventBus);
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(exchangeName).build();
    }

    @Bean
    public Queue queue() {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("").noargs();
    }
}