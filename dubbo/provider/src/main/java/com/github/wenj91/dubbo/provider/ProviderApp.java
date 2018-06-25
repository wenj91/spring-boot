package com.github.wenj91.dubbo.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class ProviderApp {
    public static void main(String...args){
        SpringApplication.run(ProviderApp.class, args);
    }
}
