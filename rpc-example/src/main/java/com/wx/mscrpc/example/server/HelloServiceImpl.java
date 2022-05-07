package com.wx.mscrpc.example.server;

import com.wx.mscrpc.example.api.Hello;
import com.wx.mscrpc.example.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
@Slf4j
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl返回: {}.", result);
        return result;
    }
}