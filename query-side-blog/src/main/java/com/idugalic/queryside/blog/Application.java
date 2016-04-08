package com.idugalic.queryside.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;

@SpringBootApplication
@EntityScan("com.idugalic.queryside.blog.domain")
public class Application {
    public static void main(String... args) {
        SpringApplication.run(Application.class, args);
    }
}