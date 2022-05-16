package com.wx.mscrpc.example.server;

import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.api.UserService;
import com.wx.mscrpc.transport.netty.server.NettyServer;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/5/16 20:32
 * @Version 1.0
 */
public class TestNettyServer2 {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        UserServiceImpl userService = new UserServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1",9998);


        nettyServer.publishService(helloService, HelloService.class);
        nettyServer.publishService(userService, UserService.class);
        nettyServer.start();
    }
}
