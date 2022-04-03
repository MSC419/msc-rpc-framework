package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.registry.DefaultServiceRegistry;
import com.wx.mscrpc.remoting.socket.RpcServer;

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
        RpcServer rpcServer = new RpcServer(defaultServiceRegistry);
        rpcServer.start(1234);
    }
}
