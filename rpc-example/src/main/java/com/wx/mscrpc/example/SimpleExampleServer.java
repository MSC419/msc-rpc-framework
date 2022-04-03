package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloServer;
import com.wx.mscrpc.server.RpcServer;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleServer {
    public static void main(String[] args) {
        HelloServer helloServer = new HelloServerImpl();
        RpcServer rpcServer = new RpcServer();
        rpcServer.register(helloServer,1234);
    }
}
