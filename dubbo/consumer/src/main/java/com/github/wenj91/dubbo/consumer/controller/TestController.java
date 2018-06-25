package com.github.wenj91.dubbo.consumer.controller;

import com.github.wenj91.dubbo.api.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
public class TestController {
    @Autowired
    private TestService testService;

    @PostMapping(value = "sayHello")
    public String sayHello(String name){
        return testService.sayHello(name);
    }
}
