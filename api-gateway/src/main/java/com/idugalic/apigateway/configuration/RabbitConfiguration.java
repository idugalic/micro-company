package com.idugalic.apigateway.configuration;

import org.axonframework.amqp.eventhandling.DefaultAMQPMessageConverter;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.serialization.Serializer;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.rabbitmq.client.Channel;

@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.hostname}")
    private String hostname;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

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

    @Profile("!cloud")
    @Bean
    ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostname);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        return connectionFactory;
    }
   
    
    @Autowired
    void rabbitAdmin(AmqpAdmin admin, ConnectionFactory connectionFactory) {
        admin.declareExchange(blogEventBusExchange());
        admin.declareQueue(eventStream());
        admin.declareBinding(blogBinding());
        admin.declareExchange(projectEventBusExchange());
        admin.declareBinding(projectBinding());
    }
   
    @Bean
    public SpringAMQPMessageSource messageSourceApiGateway(Serializer serializer) {
        return new SpringAMQPMessageSource(new DefaultAMQPMessageConverter(serializer)) {

            @RabbitListener(queues = "${spring.application.queue}")
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                super.onMessage(message, channel);
            }
        };
    }
}

