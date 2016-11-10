package com.idugalic.commandside.blog.configuration;

import com.idugalic.commandside.blog.aggregate.BlogPostAggregate;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.CommandDispatchInterceptor;
import org.axonframework.commandhandling.CommandHandlerInterceptor;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandHandlerBeanPostProcessor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.CommandGatewayFactoryBean;
import org.axonframework.commandhandling.interceptors.BeanValidationInterceptor;
import org.axonframework.commandhandling.interceptors.LoggingInterceptor;
import org.axonframework.contextsupport.spring.AnnotationDriven;
import org.axonframework.eventhandling.ClusteringEventBus;
import org.axonframework.eventhandling.DefaultClusterSelector;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.EventBusTerminal;
import org.axonframework.eventhandling.SimpleCluster;
import org.axonframework.eventhandling.amqp.spring.ListenerContainerLifecycleManager;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPConsumerConfiguration;
import org.axonframework.eventhandling.amqp.spring.SpringAMQPTerminal;
import org.axonframework.eventhandling.annotation.AnnotationEventListenerBeanPostProcessor;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.mongo.DefaultMongoTemplate;
import org.axonframework.eventstore.mongo.MongoEventStore;
import org.axonframework.eventstore.mongo.MongoTemplate;
import org.axonframework.serializer.json.JacksonSerializer;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mongodb.Mongo;

@Configuration
@AnnotationDriven
@EnableTransactionManagement
public class AxonConfiguration {

    private static final String AMQP_CONFIG_KEY = "AMQP.Config";

    @Value("${spring.application.queue}")
    private String queueName;

    @Value("${spring.application.exchange}")
    private String exchangeName;

    @Value("${spring.application.databaseName}")
    private String databaseName;

    @Value("${spring.application.eventsCollectionName}")
    private String eventsCollectionName;

    @Value("${spring.application.snapshotCollectionName}")
    private String snapshotCollectionName;

    @Bean
    JacksonSerializer axonJsonSerializer() {
        return new JacksonSerializer();
    }

    @Bean
    ListenerContainerLifecycleManager listenerContainerLifecycleManager(ConnectionFactory connectionFactory) {
        ListenerContainerLifecycleManager mgr = new ListenerContainerLifecycleManager();
        mgr.setConnectionFactory(connectionFactory);
        return mgr;
    }

    @Bean
    SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration(PlatformTransactionManager transactionManager) {
        SpringAMQPConsumerConfiguration cfg = new SpringAMQPConsumerConfiguration();
        cfg.setTransactionManager(transactionManager);
        cfg.setQueueName(queueName);
        cfg.setTxSize(10);
        return cfg;
    }

    @Bean
    SimpleCluster simpleCluster(SpringAMQPConsumerConfiguration springAMQPConsumerConfiguration) {
        SimpleCluster cluster = new SimpleCluster(queueName);
        cluster.getMetaData().setProperty(AMQP_CONFIG_KEY, springAMQPConsumerConfiguration);
        return cluster;
    }

    @Bean
    EventBusTerminal terminal(ConnectionFactory connectionFactory,  ListenerContainerLifecycleManager listenerContainerLifecycleManager) {
        SpringAMQPTerminal terminal = new SpringAMQPTerminal();
        terminal.setConnectionFactory(connectionFactory);
        terminal.setExchangeName(exchangeName);
        terminal.setDurable(true);
        terminal.setTransactional(true);
        terminal.setSerializer(axonJsonSerializer());
        terminal.setListenerContainerLifecycleManager(listenerContainerLifecycleManager);
        return terminal;
    }

    @Bean
    EventBus eventBus(SimpleCluster simpleCluster, EventBusTerminal terminal) {
        return new ClusteringEventBus(new DefaultClusterSelector(simpleCluster), terminal);
    }

    @Bean(name = "axonMongoTemplate")
    MongoTemplate axonMongoTemplate(Mongo mongo) {
        MongoTemplate template = new DefaultMongoTemplate(mongo, databaseName, eventsCollectionName, snapshotCollectionName, null, null);
        return template;
    }

    @Bean
    EventStore eventStore(MongoTemplate axonMongoTemplate, JacksonSerializer axonJsonSerializer) {
        MongoEventStore eventStore = new MongoEventStore(axonJsonSerializer, axonMongoTemplate);
        return eventStore;
    }

    @Bean
    CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        List<CommandDispatchInterceptor> dispatchInterceptors = new ArrayList<>();
        dispatchInterceptors.add(new BeanValidationInterceptor());
        List<CommandHandlerInterceptor> handlerInterceptors = new ArrayList<>();
        handlerInterceptors.add(new LoggingInterceptor());
        commandBus.setDispatchInterceptors(dispatchInterceptors);
        commandBus.setHandlerInterceptors(handlerInterceptors);
        return commandBus;
    }

    @Bean
    CommandGatewayFactoryBean<CommandGateway> commandGatewayFactoryBean(CommandBus commandBus) {
        CommandGatewayFactoryBean<CommandGateway> factory = new CommandGatewayFactoryBean<CommandGateway>();
        factory.setCommandBus(commandBus);
        return factory;
    }

    /**
     * This method allows Axon to automatically find your @EventHandler's
     *
     * @return
     */
    @Bean
    AnnotationEventListenerBeanPostProcessor eventListenerBeanPostProcessor(EventBus eventBus) {
        AnnotationEventListenerBeanPostProcessor proc = new AnnotationEventListenerBeanPostProcessor();
        proc.setEventBus(eventBus);
        return proc;
    }

    /**
     * This method allows Axon to automatically find your @CommandHandler's
     *
     * @return
     */
    @Bean
    AnnotationCommandHandlerBeanPostProcessor commandHandlerBeanPostProcessor(CommandBus commandBus) {
        AnnotationCommandHandlerBeanPostProcessor proc = new AnnotationCommandHandlerBeanPostProcessor();
        proc.setCommandBus(commandBus);
        return proc;
    }
    
    @Bean
    EventSourcingRepository<BlogPostAggregate> blogPostEventSourcingRepository(EventBus eventBus, EventStore eventStore) {
        EventSourcingRepository<BlogPostAggregate> repo = new EventSourcingRepository<BlogPostAggregate>(BlogPostAggregate.class, eventStore);
        repo.setEventBus(eventBus);
        return repo;
    }
    /**
     * This method registers your Aggregate Root as a @CommandHandler
     *
     * @return
     */
    @Bean
    AggregateAnnotationCommandHandler<BlogPostAggregate> blogPostAggregateCommandHandler(CommandBus commandBus, EventSourcingRepository<BlogPostAggregate> blogPostEventSourcingRepository) {
        @SuppressWarnings("deprecation")
        AggregateAnnotationCommandHandler<BlogPostAggregate> handler = new AggregateAnnotationCommandHandler<BlogPostAggregate>(
                BlogPostAggregate.class,
                blogPostEventSourcingRepository,
                commandBus);
        return handler;
    }

}
