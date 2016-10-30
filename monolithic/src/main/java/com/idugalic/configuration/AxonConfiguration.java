package com.idugalic.configuration;

import com.idugalic.commandside.blog.aggregate.BlogPostAggregate;
import com.idugalic.commandside.project.aggregate.ProjectAggregate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.contextsupport.spring.AnnotationDriven;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.jpa.JpaEventStore;
import org.axonframework.serializer.json.JacksonSerializer;
import org.axonframework.unitofwork.SpringTransactionManager;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@AnnotationDriven
@EnableTransactionManagement
@EntityScan(basePackages = {"org.axonframework.eventstore.jpa","com.idugalic"})
public class AxonConfiguration {
    
    @PersistenceContext(unitName = "default")
    EntityManager entityManager;


    @Bean
    JacksonSerializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    ContainerManagedEntityManagerProvider containerManagedEntityManagerProvider(){
        ContainerManagedEntityManagerProvider containerManagedEntityManagerProvider = new ContainerManagedEntityManagerProvider();
        containerManagedEntityManagerProvider.setEntityManager(entityManager);
        return containerManagedEntityManagerProvider;
    }
    
    @Bean
    public AnnotationEventListenerBeanPostProcessor annotationEventListenerBeanPostProcessor() {
        AnnotationEventListenerBeanPostProcessor processor = new AnnotationEventListenerBeanPostProcessor();
        processor.setEventBus(eventBus());
        return processor;
    }
    
    @Bean
    public AnnotationCommandHandlerBeanPostProcessor annotationCommandHandlerBeanPostProcessor(CommandBus commandBus) {
        AnnotationCommandHandlerBeanPostProcessor processor = new AnnotationCommandHandlerBeanPostProcessor();
        processor.setCommandBus(commandBus);
        return processor;
    }

    @Bean
    CommandBus commandBus(PlatformTransactionManager transactionManager) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.setTransactionManager(new SpringTransactionManager(transactionManager));
        return commandBus;
    }

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }
    
    @Bean
    JpaEventStore jpaEventStore(JacksonSerializer jacksonSerializer, ContainerManagedEntityManagerProvider containerManagedEntityManagerProvider){
        JpaEventStore jpaEventStore = new JpaEventStore(containerManagedEntityManagerProvider, jacksonSerializer);
        return jpaEventStore;
    }
    
    @Bean
    public CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean(CommandBus commandBus) {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus);
        return factory;
    }

    /**
     * This method registers your Aggregate Root as a @CommandHandler
     *
     * @return
     */
    @Bean
    AggregateAnnotationCommandHandler<BlogPostAggregate> blogPostAggregateCommandHandler(EventSourcingRepository<BlogPostAggregate> eventSourcingRepository, CommandBus commandBus) {
        @SuppressWarnings("deprecation")
        AggregateAnnotationCommandHandler<BlogPostAggregate> handler = new AggregateAnnotationCommandHandler<BlogPostAggregate>(
        		BlogPostAggregate.class,
        		eventSourcingRepository,
                commandBus);
        return handler;
    }
    
    @Bean
    EventSourcingRepository<BlogPostAggregate> blogPostEventSourcingRepository(JpaEventStore eventStore, EventBus eventBus) {
       
        EventSourcingRepository<BlogPostAggregate> repository = new EventSourcingRepository<BlogPostAggregate>(BlogPostAggregate.class, eventStore);
        repository.setEventBus(eventBus);
        return repository;
        
    }
    
    
    
    @Bean
    AggregateAnnotationCommandHandler<ProjectAggregate> projectAggregateCommandHandler(EventSourcingRepository<ProjectAggregate> eventSourcingRepository, CommandBus commandBus) {
        @SuppressWarnings("deprecation")
        AggregateAnnotationCommandHandler<ProjectAggregate> handler = new AggregateAnnotationCommandHandler<ProjectAggregate>(
        		ProjectAggregate.class,
        		eventSourcingRepository,
                commandBus);
        return handler;
    }

    @Bean
    EventSourcingRepository<ProjectAggregate> projectEventSourcingRepository(JpaEventStore eventStore, EventBus eventBus) {
        EventSourcingRepository<ProjectAggregate> repository = new EventSourcingRepository<ProjectAggregate>(ProjectAggregate.class, eventStore);
        repository.setEventBus(eventBus);
        return repository;
    }
}
