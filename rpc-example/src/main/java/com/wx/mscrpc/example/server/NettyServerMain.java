package com.wx.mscrpc.example.server;

import com.wx.mscrpc.example.api.HelloService;
import com.wx.mscrpc.transport.netty.server.NettyServer;

/**
 * @Description 用Netty做网络传输的例子服务器端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
public class NettyServerMain {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        NettyServer nettyServer = new NettyServer("127.0.0.1",9999);
        nettyServer.publishService(helloService, HelloService.class);
    }
}
