package com.idugalic.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



/**
 * Swagger 2 configuration.
 * 
 * @author idugalic
 *
 */
@Configuration
@EnableSwagger2
@Import({springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration.class})
public class SwaggerConfiguration {
    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                 .apis(RequestHandlerSelectors.basePackage("com.idugalic"))                
                .paths(PathSelectors.any())
                .build();
    }
    
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Micro-Company API")
                .description("This version of the application is deployed as a single monolithic application. Domain Driven Design is applied through Event Sourcing and CQRS. How Event Sourcing enables deployment flexibility - the application can be migrated and deployed as a microservices (checkout the master branch).")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/idugalic/micro-company/blob/master/LICENSE")
                .version("0.0.1-SNAPSHOT")
                .build();
    }


}