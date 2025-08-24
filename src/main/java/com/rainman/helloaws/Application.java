package com.rainman.helloaws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // 这个注解非常重要，它包含了 @Configuration, @EnableAutoConfiguration, @ComponentScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args); // 启动 Spring Boot 应用
    }
}