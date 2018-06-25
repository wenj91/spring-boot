package com.github.wenj91.dubbo.consumer;


import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ConsumerApp {
    public static void main(String...args){
        SpringApplication.run(ConsumerApp.class, args);
    }
}
