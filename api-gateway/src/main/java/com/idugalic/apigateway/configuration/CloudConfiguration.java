package com.idugalic.apigateway.configuration;


import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class CloudConfiguration extends AbstractCloudConfig{
    
    @Profile("cloud")
    @Bean()
    public ConnectionFactory rabbitFactory() {
        return connectionFactory().rabbitConnectionFactory();
    }
}
