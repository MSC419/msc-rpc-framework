package com.wx.mscrpc.example.server;


import com.wx.mscrpc.annotation.RpcService;
import com.wx.mscrpc.api.Hello;
import com.wx.mscrpc.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
@Slf4j
@RpcService
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl收到{}的打招呼。", hello.getName());
        log.info("打招呼内容为：{}。", hello.getMessage());
        return "打招呼成功";
    }
}
