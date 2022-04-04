package com.wx.mscrpc.example;

import com.wx.mscrpc.api.HelloService;
import com.wx.mscrpc.transport.RpcClient;
import com.wx.mscrpc.transport.RpcClientProxy;
import com.wx.mscrpc.transport.socket.SocketRpcClient;

/**
 * @Description
 * @Author MSC419
 * @Date 2022/3/29 22:48
 * @Version 1.0
 */
public class SimpleExampleClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketRpcClient("127.0.0.1",1234);
        RpcClientProxy clientProxy = new RpcClientProxy(rpcClient);
        HelloService helloService = clientProxy.getProxy(HelloService.class);
        String hello = helloService.sayHello("Uam");
        System.out.println(hello);
    }
}
