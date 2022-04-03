package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
@Slf4j
public class HelloServerImpl implements HelloServer {

    @Override
    public String sayHello(String name) {
        log.info("HelloServerImpl被调用");
        System.out.println("hello,"+name+"!");
        String result = "hello! I get your message, your name is "+name;
        log.info("返回值返回过去了："+result);
        return result;

    }
}
