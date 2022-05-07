package com.wx.mscrpc.example.server;

import com.wx.mscrpc.example.api.HelloService;
import com.wx.mscrpc.registry.DefaultServiceRegistry;
import com.wx.mscrpc.transport.socket.SocketRpcServer;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        DefaultServiceRegistry defaultServiceRegistry = new DefaultServiceRegistry();
        defaultServiceRegistry.register(helloService);
        SocketRpcServer socketRpcServer = new SocketRpcServer(defaultServiceRegistry);
        socketRpcServer.start(1234);
    }
}
