package com.wx.mscrpc.example.server;

import com.wx.mscrpc.registry.DefaultServiceRegistry;

/**
 * @Description 用Netty做网络传输的例子服务器端
 * @Author MSC419
 * @Date 2022/4/4 17:05
 * @Version 2.0
 */
public class NettyServer {
    public static void main(String[] args) {
        HelloServiceImpl helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        // 手动注册
        defaultServiceRegistry.register(helloService);
        com.wx.mscrpc.transport.netty.server.NettyServer socketRpcServer = new com.wx.mscrpc.transport.netty.server.NettyServer(1234);
        socketRpcServer.run();
    }
}
