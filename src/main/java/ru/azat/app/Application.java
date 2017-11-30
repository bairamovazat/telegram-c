package ru.azat.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@ComponentScan("ru.azat")
@EnableJpaRepositories(basePackages =  "ru.azat.repositories")
@EntityScan(basePackages = "ru.azat.models")
public class Application {
    public static void main(String[] args){
        SpringApplication.run(Application.class);
    }
}