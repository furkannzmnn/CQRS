package com.example.cqrs;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;

@SpringBootApplication
public class CqrsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CqrsApplication.class, args);
    }

    @Bean(name = "taskExecutor")
    public ScheduledExecutorService scheduledExecutorService() {
        return java.util.concurrent.Executors.newScheduledThreadPool(1);
    }

}
