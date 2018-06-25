package com.github.wenj91.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wenj91.dubbo.api.TestService;

@Service(version = "v1.0.0", group = "wenj91")
public class TestServiceImpl implements TestService {
    @Override
    public String sayHello(String name) {
        return "hello," + name;
    }
}
