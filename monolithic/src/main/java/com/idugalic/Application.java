package com.idugalic;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.idugalic.commandside.blog.configuration.AxonConfiguration;
import com.idugalic.commandside.blog.configuration.RabbitConfiguration;
import com.idugalic.commandside.blog.configuration.SecurityConfiguration;
import com.idugalic.commandside.blog.handler.EventLoggingHandler;
import com.idugalic.commandside.blog.web.RestResponseEntityExceptionHandler;
import com.idugalic.queryside.blog.configuration.RestConfiguration;
import com.idugalic.queryside.blog.repository.BlogPostRepository;

@SpringBootApplication
@Import({RestConfiguration.class})
@ComponentScan(basePackages={"com.idugalic"},excludeFilters = {
        @Filter(type = FilterType.ASSIGNABLE_TYPE,
                value = {
                		RestResponseEntityExceptionHandler.class,
                		AxonConfiguration.class,
                		com.idugalic.commandside.project.configuration.AxonConfiguration.class,
                		com.idugalic.queryside.blog.configuration.AxonConfiguration.class,
                		com.idugalic.queryside.project.configuration.AxonConfiguration.class,
                		RabbitConfiguration.class,
                		com.idugalic.commandside.project.configuration.RabbitConfiguration.class,
                		com.idugalic.queryside.blog.configuration.RabbitConfiguration.class,
                		com.idugalic.queryside.project.configuration.RabbitConfiguration.class,
                		SecurityConfiguration.class,
                		com.idugalic.commandside.project.configuration.SecurityConfiguration.class,
                		com.idugalic.queryside.blog.configuration.SecurityConfiguration.class,
                		com.idugalic.queryside.project.configuration.SecurityConfiguration.class,
                		RestConfiguration.class,
                		com.idugalic.queryside.project.configuration.RestConfiguration.class,
                		EventLoggingHandler.class,
                		com.idugalic.commandside.project.handler.EventLoggingHandler.class,
                		com.idugalic.queryside.blog.handler.EventLoggingHandler.class,
                		com.idugalic.queryside.project.handler.EventLoggingHandler.class
                		
                })
    })
public class Application {

    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String... args) throws UnknownHostException {
    	 SpringApplication app = new SpringApplication(Application.class);
         Environment env = app.run(args).getEnvironment();
         LOG.info("\n----------------------------------------------------------\n\t" +
                 "Application '{}' is running! Access URLs:\n\t" +
                 "Local: \t\thttp://127.0.0.1:{}\n\t" +
                 "External: \thttp://{}:{}\n----------------------------------------------------------",
             env.getProperty("spring.application.name"),
             env.getProperty("server.port"),
             InetAddress.getLocalHost().getHostAddress(),
             env.getProperty("server.port"));
    }
}