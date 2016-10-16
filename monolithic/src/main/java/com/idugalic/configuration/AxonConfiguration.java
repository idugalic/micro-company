package com.idugalic.configuration;

import com.idugalic.commandside.blog.aggregate.BlogPostAggregate;
import com.idugalic.commandside.project.aggregate.ProjectAggregate;

import java.io.File;
import java.util.Arrays;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.contextsupport.spring.AnnotationDriven;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AnnotationDriven
public class AxonConfiguration {


    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        AnnotationEventListenerBeanPostProcessor processor = new AnnotationEventListenerBeanPostProcessor();
        processor.setEventBus(eventBus());
        return processor;
    }
    
    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor() {
        AnnotationCommandHandlerBeanPostProcessor processor = new AnnotationCommandHandlerBeanPostProcessor();
        processor.setCommandBus(commandBus());
        return processor;
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.setHandlerInterceptors(Arrays.asList(new BeanValidationInterceptor()));
//      commandBus.setTransactionManager(new SpringTransactionManager(transactionManager));
        return commandBus;
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }
    
    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean() {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus());
        return factory;
    }

    /**
     * This method registers your Aggregate Root as a @CommandHandler
     *
     * @return
     */
    @Bean
    AggregateAnnotationCommandHandler<BlogPostAggregate> blogPostAggregateCommandHandler() {
        @SuppressWarnings("deprecation")
        AggregateAnnotationCommandHandler<BlogPostAggregate> handler = new AggregateAnnotationCommandHandler<BlogPostAggregate>(
        		BlogPostAggregate.class,
        		blogPostEventSourcingRepository(),
                commandBus());
        return handler;
    }
    
    @Bean
    EventSourcingRepository<BlogPostAggregate> blogPostEventSourcingRepository() {
        FileSystemEventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("data/evenstore/blog")));
        EventSourcingRepository<BlogPostAggregate> repository = new EventSourcingRepository<BlogPostAggregate>(BlogPostAggregate.class, eventStore);
        repository.setEventBus(eventBus());
        return repository;
    }
    
    
    
    @Bean
    AggregateAnnotationCommandHandler<ProjectAggregate> projectAggregateCommandHandler() {
        @SuppressWarnings("deprecation")
        AggregateAnnotationCommandHandler<ProjectAggregate> handler = new AggregateAnnotationCommandHandler<ProjectAggregate>(
        		ProjectAggregate.class,
        		projectEventSourcingRepository(),
                commandBus());
        return handler;
    }

    @Bean
    EventSourcingRepository<ProjectAggregate> projectEventSourcingRepository() {
        FileSystemEventStore eventStore = new FileSystemEventStore(new SimpleEventFileResolver(new File("data/evenstore/project")));
        EventSourcingRepository<ProjectAggregate> repository = new EventSourcingRepository<ProjectAggregate>(ProjectAggregate.class, eventStore);
        repository.setEventBus(eventBus());
        return repository;
    }
}
