package com.github.wenj91.dubbo.consumer.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.wenj91.dubbo.api.TestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReferenceConfig {

    @Reference(version = "v1.0.0", group = "wenj91")
    private TestService testService;

    @Bean
    public TestService testService(){
        return testService;
    }
}
