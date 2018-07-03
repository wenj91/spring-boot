package com.github.wenj91.scheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ScheduledApp {
    public static void main(String...args){
        SpringApplication.run(ScheduledApp.class, args);
    }
}
