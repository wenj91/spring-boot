package com.github.wenj91.aop.controller;

import com.github.wenj91.aop.annotation.TestAnotation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "test")
public class TestController {


    @PostMapping(value = "sayHello")
    @TestAnotation(value = "testAnotation")
    public String sayHello(String name){


        return "hello, " + name;
    }
}
